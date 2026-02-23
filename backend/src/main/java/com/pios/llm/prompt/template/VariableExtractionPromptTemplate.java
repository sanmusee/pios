package com.pios.llm.prompt.template;

import org.springframework.stereotype.Component;

/**
 * 变量提取提示词模板
 * 用于从新闻中提取结构变量
 */
@Component
public class VariableExtractionPromptTemplate extends AbstractPromptTemplate {
    
    private static final String TEMPLATE = """
请从以下新闻中提取"结构变量"：

新闻标题：${title}
新闻内容：${content}

结构变量是指能够影响市场、行业或企业的关键因素，如政策变化、资本流动、供应链变动、技术突破等。

请以JSON数组格式返回提取的变量，每个变量包含以下字段：
[
  {
    "name": "变量名称（简洁明了）",
    "type": "变量类型：policy（政策）/capital（资本）/supply（供应链）/technology（技术）/market（市场）/other（其他）",
    "direction": "影响方向：positive（正面）/negative（负面）/neutral（中性）",
    "intensity": "影响强度：1-10的整数",
    "description": "变量描述（50字以内）",
    "evidence": "原文中的证据句子"
  }
]

注意：
1. 只提取真正有影响的变量，不要强行提取
2. 如果新闻中没有明显的结构变量，返回空数组 []
3. 确保每个变量都有明确的原文证据支持
""";
    
    private static final String SYSTEM_PROMPT = """
你是一位专业的金融分析师，擅长从新闻中识别影响市场的结构变量。
请确保输出格式严格符合JSON规范，不要添加任何额外的解释文字。
""";
    
    public VariableExtractionPromptTemplate() {
        super(
            "variable-extraction",
            "变量提取提示词模板",
            "1.0.0",
            TEMPLATE,
            SYSTEM_PROMPT
        );
    }
}
