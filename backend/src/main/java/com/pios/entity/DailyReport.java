package com.pios.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "daily_report")
public class DailyReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "report_date", nullable = false, length = 10, unique = true)
    private String reportDate;
    
    @Column(length = 200)
    private String title;
    
    @Column(name = "key_variable_changes", columnDefinition = "TEXT")
    private String keyVariableChanges;
    
    @Column(name = "historical_comparison", columnDefinition = "TEXT")
    private String historicalComparison;
    
    @Column(name = "trend_inflection", columnDefinition = "TEXT")
    private String trendInflection;
    
    @Column(name = "asset_impact_matrix", columnDefinition = "TEXT")
    private String assetImpactMatrix;
    
    @Column(name = "risk_warnings", columnDefinition = "TEXT")
    private String riskWarnings;
    
    @Column(length = 800)
    private String summary;
    
    @Column(name = "word_count")
    private Integer wordCount;
    
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
