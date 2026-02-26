package com.pios.llm.agent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StructuralVariableResult {

    @JsonProperty("institutional_rule_change")
    private DimensionResult institutionalRuleChange;

    @JsonProperty("policy_direction_shift")
    private DimensionResult policyDirectionShift;

    @JsonProperty("capital_flow_change")
    private DimensionResult capitalFlowChange;

    @JsonProperty("cost_structure_change")
    private DimensionResult costStructureChange;

    @JsonProperty("supply_demand_change")
    private DimensionResult supplyDemandChange;

    @JsonProperty("technology_paradigm_shift")
    private DimensionResult technologyParadigmShift;

    @JsonProperty("behavior_expectation_shift")
    private DimensionResult behaviorExpectationShift;

    @JsonProperty("long_term_impact_over_1y")
    private DimensionResult longTermImpactOver1y;

    public boolean hasAnyStructuralChange() {
        return isYes(institutionalRuleChange) ||
               isYes(policyDirectionShift) ||
               isYes(capitalFlowChange) ||
               isYes(costStructureChange) ||
               isYes(supplyDemandChange) ||
               isYes(technologyParadigmShift) ||
               isYes(behaviorExpectationShift) ||
               isYes(longTermImpactOver1y);
    }

    private boolean isYes(DimensionResult result) {
        return result != null && "Yes".equalsIgnoreCase(result.getAnswer());
    }

    @Data
    public static class DimensionResult {
        private String answer;
        private String evidence;
    }
}
