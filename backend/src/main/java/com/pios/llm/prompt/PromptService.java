package com.pios.llm.prompt;

import com.pios.llm.client.LlmClient;
import com.pios.llm.prompt.manager.PromptManager;
import com.pios.llm.prompt.template.PromptContext;
import com.pios.llm.prompt.template.PromptTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 提示词服务
 * 提供便捷的提示词渲染和调用功能
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PromptService {
    
    private final PromptManager promptManager;
    private final LlmClient llmClient;
    
    public String execute(String templateName, PromptContext context, String provider) {
        PromptTemplate template = promptManager.getTemplate(templateName)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateName));

        String prompt = template.render(context);
        String systemPrompt = template.getSystemPrompt();

        log.debug("Executing prompt template '{}' with provider '{}', prompt length: {}",
                templateName, provider, prompt.length());

        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            return llmClient.chat(provider, prompt, systemPrompt);
        } else {
            return llmClient.chat(provider, prompt);
        }
    }
    
    /**
     * 使用默认供应商执行提示词
     */
    public String execute(String templateName, PromptContext context) {
        return execute(templateName, context, llmClient.getAvailableProviders().iterator().next());
    }
    
    /**
     * 渲染提示词（不执行）
     */
    public String render(String templateName, PromptContext context) {
        PromptTemplate template = promptManager.getTemplate(templateName)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateName));
        
        return template.render(context);
    }
    
    /**
     * 获取模板的系统提示词
     */
    public String getSystemPrompt(String templateName) {
        PromptTemplate template = promptManager.getTemplate(templateName)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateName));
        
        return template.getSystemPrompt();
    }
}
