package com.pios.entity.macro;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("macro_data")
public class MacroData {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long indicatorId;

    private LocalDate period;

    private BigDecimal value;

    private LocalDateTime createdAt;
}
