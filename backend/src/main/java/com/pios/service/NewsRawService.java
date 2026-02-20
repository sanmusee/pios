package com.pios.service;

import com.pios.entity.NewsRaw;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsRawService {
    
    NewsRaw save(NewsRaw newsRaw);
    
    Optional<NewsRaw> findById(Long id);
    
    Optional<NewsRaw> findByUrl(String url);
    
    List<NewsRaw> findAll();
    
    List<NewsRaw> findBySource(String source, Integer limit);
    
    List<NewsRaw> findByCategory(String category, Integer limit);
    
    List<NewsRaw> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    void deleteById(Long id);
    
    Long countToday();
    
    boolean existsByUrl(String url);
}
