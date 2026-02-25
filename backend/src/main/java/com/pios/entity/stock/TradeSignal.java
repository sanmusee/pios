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
@TableName("trade_signal")
public class TradeSignal {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("code")
    private String code;

    @TableField("signal_date")
    private LocalDate signalDate;

    @TableField("signal_type")
    private String signalType;

    @TableField("total_score_snapshot")
    private BigDecimal totalScoreSnapshot;

    @TableField("reason")
    private String reason;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
