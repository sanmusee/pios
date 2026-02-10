package com.pios.repository;

import com.pios.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {
    List<DataSource> findByStatus(String status);
    List<DataSource> findByType(String type);
}
