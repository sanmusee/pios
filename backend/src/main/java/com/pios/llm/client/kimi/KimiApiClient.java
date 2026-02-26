package com.pios.llm.client.kimi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Kimi API 客户端
 * 直接调用 Moonshot Kimi API
 */
@Slf4j
@Component
public class KimiApiClient {
    
    private static final String API_URL = "https://api.moonshot.cn/v1/chat/completions";
    
    @Value("${pios.llm.kimi.api-key:}")
    private String apiKey;
    
    @Value("${pios.llm.kimi.model:kimi-k2.5}")
    private String defaultModel;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public KimiApiClient() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * 发送聊天请求
     */
    public String chat(String prompt) {
        return chat(prompt, defaultModel, null);
    }
    
    /**
     * 发送聊天请求（带系统提示词）
     */
    public String chat(String prompt, String systemPrompt) {
        return chat(prompt, defaultModel, systemPrompt);
    }
    
    /**
     * 发送聊天请求
     */
    public String chat(String prompt, String model, String systemPrompt) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("Kimi API Key not configured");
        }
        
        try {
            // 构建请求
            KimiChatRequest request = new KimiChatRequest();
            request.setModel(model != null ? model : defaultModel);
            request.setTemperature(1.0);
            
            List<KimiChatRequest.Message> messages = new ArrayList<>();
            
            // 添加系统提示词
            if (systemPrompt != null && !systemPrompt.isEmpty()) {
                messages.add(new KimiChatRequest.Message("system", systemPrompt));
            }
            
            // 添加用户消息
            messages.add(new KimiChatRequest.Message("user", prompt));
            request.setMessages(messages);
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            
            // 发送请求
            HttpEntity<KimiChatRequest> entity = new HttpEntity<>(request, headers);
            
            log.debug("Sending request to Kimi API, model: {}", request.getModel());
            
            ResponseEntity<KimiChatResponse> response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                entity,
                KimiChatResponse.class
            );
            
            // 解析响应
            KimiChatResponse body = response.getBody();
            if (body == null || body.getChoices() == null || body.getChoices().isEmpty()) {
                throw new RuntimeException("Empty response from Kimi API");
            }
            
            String content = body.getChoices().get(0).getMessage().getContent();
            log.debug("Received response from Kimi API, content length: {}", content.length());
            
            return content;
            
        } catch (Exception e) {
            log.error("Failed to call Kimi API", e);
            throw new RuntimeException("Kimi API call failed: " + e.getMessage(), e);
        }
    }
}
