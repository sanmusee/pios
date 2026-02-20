package com.pios.service.impl;

import com.pios.entity.NewsRaw;
import com.pios.mapper.NewsRawMapper;
import com.pios.service.NewsRawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NewsRawServiceImpl implements NewsRawService {
    
    @Autowired
    private NewsRawMapper newsRawMapper;
    
    @Override
    public NewsRaw save(NewsRaw newsRaw) {
        newsRawMapper.insert(newsRaw);
        return newsRaw;
    }
    
    @Override
    public Optional<NewsRaw> findById(Long id) {
        return Optional.ofNullable(newsRawMapper.selectById(id));
    }
    
    @Override
    public Optional<NewsRaw> findByUrl(String url) {
        return Optional.ofNullable(newsRawMapper.selectByUrl(url));
    }
    
    @Override
    public List<NewsRaw> findAll() {
        return newsRawMapper.selectList(null);
    }
    
    @Override
    public List<NewsRaw> findBySource(String source, Integer limit) {
        return newsRawMapper.selectBySource(source, limit);
    }
    
    @Override
    public List<NewsRaw> findByCategory(String category, Integer limit) {
        return newsRawMapper.selectByCategory(category, limit);
    }
    
    @Override
    public List<NewsRaw> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return newsRawMapper.selectByTimeRange(startTime, endTime);
    }
    
    @Override
    public void deleteById(Long id) {
        newsRawMapper.deleteById(id);
    }
    
    @Override
    public Long countToday() {
        return newsRawMapper.countToday();
    }
    
    @Override
    public boolean existsByUrl(String url) {
        return newsRawMapper.selectByUrl(url) != null;
    }
}
