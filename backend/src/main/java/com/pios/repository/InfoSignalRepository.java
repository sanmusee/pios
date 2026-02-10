package com.pios.repository;

import com.pios.entity.InfoSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoSignalRepository extends JpaRepository<InfoSignal, Long> {
    List<InfoSignal> findByCategory(String category);
    List<InfoSignal> findByLevel(Integer level);
    List<InfoSignal> findBySource(String source);
    List<InfoSignal> findByTitleAndSource(String title, String source);
}
