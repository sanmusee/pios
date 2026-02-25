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
@TableName("industry_financial_summary")
public class IndustryFinancialSummary {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("industry_id")
    private Integer industryId;

    @TableField("report_date")
    private LocalDate reportDate;

    @TableField("revenue_growth")
    private BigDecimal revenueGrowth;

    @TableField("profit_growth")
    private BigDecimal profitGrowth;

    @TableField("profit_acceleration")
    private BigDecimal profitAcceleration;

    @TableField("inventory_change")
    private BigDecimal inventoryChange;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
