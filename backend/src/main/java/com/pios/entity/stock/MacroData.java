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
@TableName("macro_data")
public class MacroData {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("indicator_code")
    private String indicatorCode;

    @TableField("indicator_name")
    private String indicatorName;

    @TableField("period_date")
    private LocalDate periodDate;

    @TableField("value")
    private BigDecimal value;

    @TableField("yoy")
    private BigDecimal yoy;

    @TableField("acceleration")
    private BigDecimal acceleration;

    @TableField("frequency")
    private String frequency;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
