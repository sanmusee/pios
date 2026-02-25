package com.pios.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("info_signal")
public class InfoSignal {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("source")
    private String source;

    @TableField("category")
    private String category;

    @TableField("level")
    private Integer level;

    @TableField("tags")
    private String tags;

    @TableField("signal_time")
    private LocalDateTime signalTime;

    @TableField("original_summary")
    private String originalSummary;

    @TableField("original_content")
    private String originalContent;

    @TableField("original_url")
    private String originalUrl;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
