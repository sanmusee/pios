package com.pios.repository;

import com.pios.entity.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
    Optional<DailyReport> findByReportDate(String reportDate);
}
