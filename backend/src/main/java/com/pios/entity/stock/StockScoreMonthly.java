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
@TableName("stock_score_monthly")
public class StockScoreMonthly {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("code")
    private String code;

    @TableField("score_date")
    private LocalDate scoreDate;

    @TableField("macro_score")
    private BigDecimal macroScore;

    @TableField("industry_score")
    private BigDecimal industryScore;

    @TableField("quality_score")
    private BigDecimal qualityScore;

    @TableField("valuation_score")
    private BigDecimal valuationScore;

    @TableField("trend_score")
    private BigDecimal trendScore;

    @TableField("total_score")
    private BigDecimal totalScore;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
