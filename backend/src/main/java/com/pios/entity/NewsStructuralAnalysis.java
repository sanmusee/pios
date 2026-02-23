package com.pios.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 新闻结构变量分析结果
 * 记录每篇新闻的结构变量识别结果
 */
@Data
@Entity
@Table(name = "news_structural_analysis")
public class NewsStructuralAnalysis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 关联的新闻ID
     */
    @Column(name = "news_raw_id", nullable = false)
    private Long newsRawId;
    
    /**
     * 关联的新闻（可选）
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_raw_id", insertable = false, updatable = false)
    private NewsRaw newsRaw;
    
    // ========== 结构性变量识别结果（8个维度）==========
    
    /**
     * 制度或规则改变
     */
    @Column(name = "institutional_rule_change", length = 10)
    private String institutionalRuleChange;
    
    @Column(name = "institutional_rule_evidence", columnDefinition = "TEXT")
    private String institutionalRuleEvidence;
    
    /**
     * 监管或政策方向改变
     */
    @Column(name = "policy_direction_shift", length = 10)
    private String policyDirectionShift;
    
    @Column(name = "policy_direction_evidence", columnDefinition = "TEXT")
    private String policyDirectionEvidence;
    
    /**
     * 资本流向改变
     */
    @Column(name = "capital_flow_change", length = 10)
    private String capitalFlowChange;
    
    @Column(name = "capital_flow_evidence", columnDefinition = "TEXT")
    private String capitalFlowEvidence;
    
    /**
     * 产业成本结构改变
     */
    @Column(name = "cost_structure_change", length = 10)
    private String costStructureChange;
    
    @Column(name = "cost_structure_evidence", columnDefinition = "TEXT")
    private String costStructureEvidence;
    
    /**
     * 供需关系改变
     */
    @Column(name = "supply_demand_change", length = 10)
    private String supplyDemandChange;
    
    @Column(name = "supply_demand_evidence", columnDefinition = "TEXT")
    private String supplyDemandEvidence;
    
    /**
     * 技术范式改变
     */
    @Column(name = "technology_paradigm_shift", length = 10)
    private String technologyParadigmShift;
    
    @Column(name = "technology_paradigm_evidence", columnDefinition = "TEXT")
    private String technologyParadigmEvidence;
    
    /**
     * 行为预期可能发生群体级变化
     */
    @Column(name = "behavior_expectation_shift", length = 10)
    private String behaviorExpectationShift;
    
    @Column(name = "behavior_expectation_evidence", columnDefinition = "TEXT")
    private String behaviorExpectationEvidence;
    
    /**
     * 影响可能持续超过一年
     */
    @Column(name = "long_term_impact_over_1y", length = 10)
    private String longTermImpactOver1y;
    
    @Column(name = "long_term_impact_evidence", columnDefinition = "TEXT")
    private String longTermImpactEvidence;
    
    // ========== 分析元数据 ==========
    
    /**
     * 是否存在任何结构性变量变化
     */
    @Column(name = "has_structural_change")
    private Boolean hasStructuralChange;
    
    /**
     * 变化维度数量（Yes 的数量）
     */
    @Column(name = "change_dimension_count")
    private Integer changeDimensionCount;
    
    /**
     * 使用的LLM供应商
     */
    @Column(name = "llm_provider", length = 50)
    private String llmProvider;
    
    /**
     * 分析状态：pending/processing/completed/failed
     */
    @Column(name = "analysis_status", length = 20)
    private String analysisStatus;
    
    /**
     * 分析时间
     */
    @Column(name = "analysis_time")
    private LocalDateTime analysisTime;
    
    /**
     * 错误信息（如果分析失败）
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    /**
     * 原始LLM响应（用于调试）
     */
    @Column(name = "raw_llm_response", columnDefinition = "LONGTEXT")
    private String rawLlmResponse;
    
    // ========== 时间戳 ==========
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
