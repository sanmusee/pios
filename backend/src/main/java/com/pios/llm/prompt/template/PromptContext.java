package com.pios.llm.prompt.template;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 提示词上下文
 * 用于传递模板渲染所需的参数
 */
@Data
@Builder
public class PromptContext {
    
    /**
     * 模板变量
     */
    @Builder.Default
    private Map<String, Object> variables = new HashMap<>();
    
    /**
     * 添加变量
     */
    public PromptContext addVariable(String key, Object value) {
        variables.put(key, value);
        return this;
    }
    
    /**
     * 获取变量
     */
    public Object getVariable(String key) {
        return variables.get(key);
    }
    
    /**
     * 获取字符串变量
     */
    public String getString(String key) {
        Object value = variables.get(key);
        return value != null ? value.toString() : null;
    }
    
    /**
     * 创建空上下文
     */
    public static PromptContext empty() {
        return PromptContext.builder().build();
    }
}
