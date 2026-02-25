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
@TableName("stock_price_daily")
public class StockPriceDaily {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("code")
    private String code;

    @TableField("trade_date")
    private LocalDate tradeDate;

    @TableField("close_price")
    private BigDecimal closePrice;

    @TableField("volume")
    private Long volume;

    @TableField("market_cap")
    private BigDecimal marketCap;

    @TableField("pe_ttm")
    private BigDecimal peTtm;

    @TableField("pb")
    private BigDecimal pb;

    @TableField("ma20")
    private BigDecimal ma20;

    @TableField("ma120")
    private BigDecimal ma120;

    @TableField("relative_strength")
    private BigDecimal relativeStrength;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
