package com.pios.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inference_result")
public class InferenceResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "correlation_chain_id")
    private Long correlationChainId;
    
    @Column(columnDefinition = "TEXT")
    private String meaning;
    
    @Column(name = "chain_effects", columnDefinition = "TEXT")
    private String chainEffects;
    
    @Column(columnDefinition = "TEXT")
    private String beneficiaries;
    
    @Column(columnDefinition = "TEXT")
    private String losers;
    
    @Column(name = "time_scale", length = 20)
    private String timeScale;
    
    @Column(name = "confidence_level", length = 20)
    private String confidenceLevel;
    
    @Column(name = "inference_date")
    private LocalDateTime inferenceDate;
    
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
