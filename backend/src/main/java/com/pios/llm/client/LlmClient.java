package com.pios.llm.client;

import com.pios.llm.client.kimi.KimiApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LlmClient {
    
    private static final Logger log = LoggerFactory.getLogger(LlmClient.class);
    
    @Autowired
    private KimiApiClient kimiApiClient;
    
    private static final String DEFAULT_PROVIDER = "kimi";
    private static final Set<String> AVAILABLE_PROVIDERS = Set.of("kimi");
    
    public String chat(String provider, String prompt) {
        if (!"kimi".equals(provider)) {
            throw new IllegalArgumentException("Only 'kimi' provider is supported, got: " + provider);
        }
        
        log.debug("Calling LLM provider '{}' with prompt length: {}", provider, prompt.length());
        String response = kimiApiClient.chat(prompt);
        log.debug("LLM response received, length: {}", response.length());
        
        return response;
    }
    
    public String chat(String provider, String prompt, String systemPrompt) {
        if (!"kimi".equals(provider)) {
            throw new IllegalArgumentException("Only 'kimi' provider is supported, got: " + provider);
        }
        
        log.debug("Calling LLM provider '{}' with system prompt", provider);
        String response = kimiApiClient.chat(prompt, systemPrompt);
        log.debug("LLM response received, length: {}", response.length());
        
        return response;
    }
    
    public String chat(String prompt) {
        return chat(DEFAULT_PROVIDER, prompt);
    }
    
    public String chatWithSystemPrompt(String prompt, String systemPrompt) {
        return chat(DEFAULT_PROVIDER, prompt, systemPrompt);
    }
    
    public boolean isAvailable(String provider) {
        return "kimi".equals(provider);
    }
    
    public Set<String> getAvailableProviders() {
        return AVAILABLE_PROVIDERS;
    }
}
