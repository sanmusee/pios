package com.pios.entity.stock;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("industry_score_monthly")
public class IndustryScoreMonthly {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("industry_id")
    private Integer industryId;

    @TableField("score_date")
    private LocalDate scoreDate;

    @TableField("macro_score")
    private BigDecimal macroScore;

    @TableField("profit_score")
    private BigDecimal profitScore;

    @TableField("valuation_score")
    private BigDecimal valuationScore;

    @TableField("trend_score")
    private BigDecimal trendScore;

    @TableField("total_score")
    private BigDecimal totalScore;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
