package com.pios.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("news_raw")
public class NewsRaw {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("source")
    private String source;

    @TableField("publish_time")
    private LocalDateTime publishTime;

    @TableField("category")
    private String category;

    @TableField("url")
    private String url;

    @TableField("fetch_time")
    private Date fetchTime;
}
