package com.pios.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "correlation_chain")
public class CorrelationChain {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "source_signal_id")
    private Long sourceSignalId;
    
    @Column(name = "related_variables", columnDefinition = "TEXT")
    private String relatedVariables;
    
    @Column(name = "causal_logic", columnDefinition = "TEXT")
    private String causalLogic;
    
    @Column(name = "historical_comparison", columnDefinition = "TEXT")
    private String historicalComparison;
    
    @Column(name = "cycle_phase", length = 100)
    private String cyclePhase;
    
    private Integer status;
    
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
