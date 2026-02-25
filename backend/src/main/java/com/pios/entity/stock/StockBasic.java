package com.pios.entity.stock;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("stock_basic")
public class StockBasic {

    @TableId(value = "code", type = IdType.INPUT)
    private String code;

    @TableField("name")
    private String name;

    @TableField("exchange")
    private String exchange;

    @TableField("industry_id")
    private Integer industryId;

    @TableField("list_date")
    private LocalDate listDate;

    @TableField("is_st")
    private Integer isSt;

    @TableField("status")
    private String status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
