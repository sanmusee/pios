package com.pios.llm.prompt.template;

import org.springframework.stereotype.Component;

/**
 * 结构性变量识别提示词模板
 * 用于识别文章中的结构性变量变化
 */
@Component
public class StructuralVariableRecognitionPromptTemplate extends AbstractPromptTemplate {
    
    private static final String TEMPLATE = """
你是"结构性信息识别引擎"。

你的任务不是评价文章好坏，
也不是预测未来，
而是判断文章是否包含"已经发生的、可验证的结构性变量变化"。

结构性变量定义：
1. 制度或规则改变
2. 监管或政策方向改变
3. 资本流向改变
4. 产业成本结构改变
5. 供需关系改变
6. 技术范式改变
7. 行为预期可能发生群体级变化
8. 影响可能持续超过一年

重要约束：
- 只允许基于文章中已经发生的事实判断
- 专家观点不计入
- 预测不计入
- 评论性语言不计入
- 情绪表达不计入
- 未落地政策不计入
- 未正式发布的传闻不计入

你只能识别"变量变化"，不能进行价值判断。如果文章只是讨论"未来可能"、"专家认为"、"趋势预测"，
且未发生明确事实变化，必须全部回答为 "No"。

所有判断必须引用原文中的一句完整句子作为证据。

禁止输出任何解释性段落。
禁止总结。
禁止推测。
只输出指定JSON结构。

请根据结构性变量标准，
判断下列文章在各维度上是否存在"已发生的变量变化"。

回答要求：
- 只输出 JSON
- 每个字段 answer 只能是 "Yes" / "No" / "Partial"
- 必须提供原文证据句
- 若无明确证据，必须为 "No"

文章如下：

${articleText}

输出格式：
{
  "institutional_rule_change": {
    "answer": "Yes | No | Partial",
    "evidence": "原文中的完整句子"
  },
  "policy_direction_shift": {
    "answer": "",
    "evidence": ""
  },
  "capital_flow_change": {
    "answer": "",
    "evidence": ""
  },
  "cost_structure_change": {
    "answer": "",
    "evidence": ""
  },
  "supply_demand_change": {
    "answer": "",
    "evidence": ""
  },
  "technology_paradigm_shift": {
    "answer": "",
    "evidence": ""
  },
  "behavior_expectation_shift": {
    "answer": "",
    "evidence": ""
  },
  "long_term_impact_over_1y": {
    "answer": "",
    "evidence": ""
  }
}
""";
    
    private static final String SYSTEM_PROMPT = """
你是结构性信息识别引擎，专门识别文章中已经发生的、可验证的结构性变量变化。
严格遵守约束条件，只基于事实判断，不输出任何解释性内容。
确保输出格式为纯JSON，不包含任何markdown代码块标记。
""";
    
    public StructuralVariableRecognitionPromptTemplate() {
        super(
            "structural-variable-recognition",
            "结构性变量识别提示词模板",
            "1.0.0",
            TEMPLATE,
            SYSTEM_PROMPT
        );
    }
}
