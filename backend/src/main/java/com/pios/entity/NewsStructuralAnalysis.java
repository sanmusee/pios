package com.pios.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("news_structural_analysis")
public class NewsStructuralAnalysis {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("news_raw_id")
    private Long newsRawId;

    @TableField("institutional_rule_change")
    private String institutionalRuleChange;

    @TableField("institutional_rule_evidence")
    private String institutionalRuleEvidence;

    @TableField("policy_direction_shift")
    private String policyDirectionShift;

    @TableField("policy_direction_evidence")
    private String policyDirectionEvidence;

    @TableField("capital_flow_change")
    private String capitalFlowChange;

    @TableField("capital_flow_evidence")
    private String capitalFlowEvidence;

    @TableField("cost_structure_change")
    private String costStructureChange;

    @TableField("cost_structure_evidence")
    private String costStructureEvidence;

    @TableField("supply_demand_change")
    private String supplyDemandChange;

    @TableField("supply_demand_evidence")
    private String supplyDemandEvidence;

    @TableField("technology_paradigm_shift")
    private String technologyParadigmShift;

    @TableField("technology_paradigm_evidence")
    private String technologyParadigmEvidence;

    @TableField("behavior_expectation_shift")
    private String behaviorExpectationShift;

    @TableField("behavior_expectation_evidence")
    private String behaviorExpectationEvidence;

    @TableField("long_term_impact_over_1y")
    private String longTermImpactOver1y;

    @TableField("long_term_impact_evidence")
    private String longTermImpactEvidence;

    @TableField("has_structural_change")
    private Boolean hasStructuralChange;

    @TableField("change_dimension_count")
    private Integer changeDimensionCount;

    @TableField("llm_provider")
    private String llmProvider;

    @TableField("analysis_status")
    private String analysisStatus;

    @TableField("analysis_time")
    private LocalDateTime analysisTime;

    @TableField("error_message")
    private String errorMessage;

    @TableField("raw_llm_response")
    private String rawLlmResponse;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
