package com.pios.entity.stock;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("stock_financial_raw")
public class StockFinancialRaw {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("code")
    private String code;

    @TableField("report_date")
    private Date reportDate;

    @TableField("revenue")
    private BigDecimal revenue;

    @TableField("net_profit")
    private BigDecimal netProfit;

    @TableField("roe")
    private BigDecimal roe;

    @TableField("gross_margin")
    private BigDecimal grossMargin;

    @TableField("debt_ratio")
    private BigDecimal debtRatio;

    @TableField("operating_cashflow")
    private BigDecimal operatingCashflow;

    @TableField("yoy_revenue")
    private BigDecimal yoyRevenue;

    @TableField("yoy_profit")
    private BigDecimal yoyProfit;

    @TableField("created_at")
    private Date createdAt;
}
