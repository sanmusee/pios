package com.pios.llm.client.kimi;

import lombok.Data;
import java.util.List;

/**
 * Kimi API 聊天完成请求
 */
@Data
public class KimiChatRequest {
    
    private String model;
    private List<Message> messages;
    private Double temperature;
    private Integer maxTokens;
    
    @Data
    public static class Message {
        private String role;
        private String content;
        
        public Message() {}
        
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
