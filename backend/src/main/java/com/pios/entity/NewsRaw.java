package com.pios.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 原始新闻信息表
 * 对应数据库表: news_raw
 */
@Data
@Entity
@Table(name = "news_raw")
public class NewsRaw {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 文章标题
     */
    @Column(nullable = false, length = 500)
    private String title;
    
    /**
     * 文章正文（纯文本）
     */
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;
    
    /**
     * 来源，如36kr、gov、pbc
     */
    @Column(nullable = false, length = 100)
    private String source;
    
    /**
     * 原始发布时间
     */
    @Column(name = "publish_time", nullable = false)
    private LocalDateTime publishTime;
    
    /**
     * 分类，如宏观/科技/产业
     */
    @Column(length = 100)
    private String category;
    
    /**
     * 原文链接
     */
    @Column(nullable = false, length = 512, unique = true)
    private String url;
    
    /**
     * 采集时间
     */
    @Column(name = "fetch_time", nullable = false)
    private LocalDateTime fetchTime;
    
    @PrePersist
    protected void onCreate() {
        if (fetchTime == null) {
            fetchTime = LocalDateTime.now();
        }
    }
}
