package com.pios.repository;

import com.pios.entity.CorrelationChain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorrelationChainRepository extends JpaRepository<CorrelationChain, Long> {
    List<CorrelationChain> findByStatus(Integer status);
    List<CorrelationChain> findBySourceSignalId(Long sourceSignalId);
}
