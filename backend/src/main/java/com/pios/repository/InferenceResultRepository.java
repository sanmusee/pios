package com.pios.repository;

import com.pios.entity.InferenceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InferenceResultRepository extends JpaRepository<InferenceResult, Long> {
    List<InferenceResult> findByCorrelationChainId(Long correlationChainId);
    List<InferenceResult> findByConfidenceLevel(String confidenceLevel);
}
