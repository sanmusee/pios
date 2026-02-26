package com.pios.llm.prompt.template;

import org.springframework.stereotype.Component;

/**
 * 新闻分析提示词模板
 * 用于分析新闻内容，提取关键信息
 */
@Component
public class NewsAnalysisPromptTemplate extends AbstractPromptTemplate {
    
    private static final String TEMPLATE = """
请分析以下新闻的关键信息：

新闻标题：${title}
新闻内容：${content}

请以JSON格式返回以下信息：
{
  "title": "新闻标题",
  "summary": "新闻摘要（100字以内）",
  "impactArea": ["影响领域1", "影响领域2"],
  "policyDirection": "positive/negative/neutral",
  "keyPoints": ["关键点1", "关键点2", "关键点3"],
  "entities": ["涉及实体1", "涉及实体2"]
}
""";
    
    private static final String SYSTEM_PROMPT = """
你是一位专业的新闻分析师，擅长从新闻中提取关键信息并进行结构化分析。
请确保输出格式严格符合JSON规范，不要添加任何额外的解释文字。
""";
    
    public NewsAnalysisPromptTemplate() {
        super(
            "news-analysis",
            "新闻分析提示词模板",
            "1.0.0",
            TEMPLATE,
            SYSTEM_PROMPT
        );
    }
}
