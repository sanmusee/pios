package com.pios.entity.macro;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("macro_indicator")
public class MacroIndicator {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String code;

    private String name;

    private Long categoryId;

    private String statType;

    private String unit;

    private String frequency;
}
