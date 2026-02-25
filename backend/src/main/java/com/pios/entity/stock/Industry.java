package com.pios.entity.stock;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("industry")
public class Industry {

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("level")
    private Integer level;

    @TableField("parent_id")
    private Integer parentId;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
