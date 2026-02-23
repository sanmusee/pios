package com.pios.llm.prompt.template;

/**
 * 提示词模板接口
 * 定义提示词模板的基本行为
 */
public interface PromptTemplate {
    
    /**
     * 获取模板名称
     */
    String getName();
    
    /**
     * 获取模板描述
     */
    String getDescription();
    
    /**
     * 获取模板版本
     */
    String getVersion();
    
    /**
     * 渲染提示词
     * @param context 上下文参数
     * @return 渲染后的提示词
     */
    String render(PromptContext context);
    
    /**
     * 获取系统提示词（可选）
     */
    default String getSystemPrompt() {
        return null;
    }
}
