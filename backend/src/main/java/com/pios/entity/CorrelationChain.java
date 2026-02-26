package com.pios.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("correlation_chain")
public class CorrelationChain {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("source_signal_id")
    private Long sourceSignalId;

    @TableField("related_variables")
    private String relatedVariables;

    @TableField("causal_logic")
    private String causalLogic;

    @TableField("historical_comparison")
    private String historicalComparison;

    @TableField("cycle_phase")
    private String cyclePhase;

    @TableField("status")
    private Integer status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
