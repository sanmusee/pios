package com.pios.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "info_signal")
public class InfoSignal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 500)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(length = 100)
    private String source;
    
    @Column(length = 50)
    private String category;
    
    private Integer level;
    
    @Column(length = 500)
    private String tags;
    
    @Column(name = "signal_time")
    private LocalDateTime signalTime;

    @Column(name = "original_summary", columnDefinition = "TEXT")
    private String originalSummary;

    @Column(name = "original_content", columnDefinition = "TEXT")
    private String originalContent;

    @Column(name = "original_url", length = 1000)
    private String originalUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
