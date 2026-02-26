package com.pios.llm.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pios.llm.agent.model.StructuralVariableResult;
import com.pios.llm.prompt.PromptService;
import com.pios.llm.prompt.template.PromptContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 结构性变量识别 Agent
 * 使用 LLM 识别文章中的结构性变量变化
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StructuralVariableAgent {
    
    private final PromptService promptService;
    private final ObjectMapper objectMapper;
    
    private static final String TEMPLATE_NAME = "structural-variable-recognition";
    
    /**
     * 分析文章内容，识别结构性变量变化
     * @param articleText 文章内容
     * @param provider LLM 供应商
     * @return 识别结果
     */
    public StructuralVariableResult analyze(String articleText, String provider) {
        try {
            PromptContext context = PromptContext.builder()
                    .variables(new java.util.HashMap<>())
                    .build();
            context.addVariable("articleText", articleText);
            
            String response = promptService.execute(TEMPLATE_NAME, context, provider);
            
            // 清理响应，移除可能的 markdown 代码块标记
            String cleanedResponse = cleanJsonResponse(response);

            StructuralVariableResult result = objectMapper.readValue(cleanedResponse, StructuralVariableResult.class);
            return result;
        } catch (Exception e) {
            log.error("Failed to analyze structural variables", e);
            throw new RuntimeException("结构性变量分析失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 使用默认供应商分析
     */
    public StructuralVariableResult analyze(String articleText) {
        return analyze(articleText, null);
    }
    
    /**
     * 清理 JSON 响应，移除 markdown 代码块等
     */
    private String cleanJsonResponse(String response) {
        if (response == null) {
            return "";
        }
        
        // 移除 markdown 代码块标记
        String cleaned = response.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        
        return cleaned.trim();
    }
}
