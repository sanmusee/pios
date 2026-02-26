package com.pios.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("inference_result")
public class InferenceResult {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("correlation_chain_id")
    private Long correlationChainId;

    @TableField("meaning")
    private String meaning;

    @TableField("chain_effects")
    private String chainEffects;

    @TableField("beneficiaries")
    private String beneficiaries;

    @TableField("losers")
    private String losers;

    @TableField("time_scale")
    private String timeScale;

    @TableField("confidence_level")
    private String confidenceLevel;

    @TableField("inference_date")
    private LocalDateTime inferenceDate;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
