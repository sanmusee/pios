package com.pios.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("daily_report")
public class DailyReport {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("report_date")
    private String reportDate;

    @TableField("title")
    private String title;

    @TableField("key_variable_changes")
    private String keyVariableChanges;

    @TableField("historical_comparison")
    private String historicalComparison;

    @TableField("trend_inflection")
    private String trendInflection;

    @TableField("asset_impact_matrix")
    private String assetImpactMatrix;

    @TableField("risk_warnings")
    private String riskWarnings;

    @TableField("summary")
    private String summary;

    @TableField("word_count")
    private Integer wordCount;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
