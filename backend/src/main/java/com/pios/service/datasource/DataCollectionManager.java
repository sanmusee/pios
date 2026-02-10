package com.pios.service.datasource;

import com.pios.entity.DataSource;
import com.pios.entity.InfoSignal;
import com.pios.repository.DataSourceRepository;
import com.pios.repository.InfoSignalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据采集管理器
 * 统一管理所有数据源的采集任务
 */
@Slf4j
@Service
public class DataCollectionManager {

    @Autowired
    private InfoSignalRepository infoSignalRepository;
    
    @Autowired
    private DataSourceRepository dataSourceRepository;
    
    @Autowired
    private List<DataSourceCollector> collectors;

    /**
     * 初始化数据源配置
     */
    @PostConstruct
    public void init() {
        log.info("初始化数据采集管理器，发现 {} 个采集器", collectors.size());
        
        // 注册数据源
        for (DataSourceCollector collector : collectors) {
            registerDataSource(collector);
        }
    }

    /**
     * 注册数据源
     */
    private void registerDataSource(DataSourceCollector collector) {
        try {
            List<DataSource> existing = dataSourceRepository.findByType(collector.getSourceType());
            
            if (existing.isEmpty()) {
                DataSource dataSource = new DataSource();
                dataSource.setName(collector.getSourceName());
                dataSource.setType(collector.getSourceType());
                dataSource.setStatus(collector.isAvailable() ? "ACTIVE" : "INACTIVE");
                dataSource.setCreatedAt(LocalDateTime.now());
                dataSource.setUpdatedAt(LocalDateTime.now());
                
                dataSourceRepository.save(dataSource);
                log.info("注册新数据源: {}", collector.getSourceName());
            } else {
                log.info("数据源已存在: {}", collector.getSourceName());
            }
        } catch (Exception e) {
            log.error("注册数据源失败: {}", collector.getSourceName(), e);
        }
    }

    /**
     * 执行所有数据源的采集任务
     * 每30分钟执行一次
     */
    @Scheduled(cron = "0 */30 * * * ?")
    public void collectAll() {
        log.info("开始执行定时数据采集任务");
        
        for (DataSourceCollector collector : collectors) {
            if (collector.isAvailable()) {
                try {
                    collectFromSource(collector);
                } catch (Exception e) {
                    log.error("从 {} 采集数据失败", collector.getSourceName(), e);
                }
            } else {
                log.warn("数据源 {} 不可用，跳过采集", collector.getSourceName());
            }
        }
        
        log.info("定时数据采集任务完成");
    }

    /**
     * 从指定数据源采集数据
     */
    public void collectFromSource(DataSourceCollector collector) {
        String sourceName = collector.getSourceName();
        log.info("开始从 {} 采集数据", sourceName);
        
        try {
            // 更新数据源最后采集时间
            updateDataSourceCollectTime(collector.getSourceType());
            
            // 执行采集
            List<InfoSignal> signals = collector.collect();
            
            // 保存采集到的信号
            int savedCount = 0;
            for (InfoSignal signal : signals) {
                if (signal != null && signal.getTitle() != null) {
                    // 检查是否已存在相同标题的信号（去重）
                    if (!isSignalExists(signal)) {
                        infoSignalRepository.save(signal);
                        savedCount++;
                    }
                }
            }
            
            log.info("从 {} 采集到 {} 条信号，保存 {} 条", sourceName, signals.size(), savedCount);
            
        } catch (Exception e) {
            log.error("从 {} 采集数据时发生错误", sourceName, e);
            throw e;
        }
    }

    /**
     * 手动触发指定类型的数据源采集
     */
    public void collectByType(String sourceType) {
        log.info("手动触发 {} 类型数据源采集", sourceType);
        
        for (DataSourceCollector collector : collectors) {
            if (collector.getSourceType().equalsIgnoreCase(sourceType) && collector.isAvailable()) {
                collectFromSource(collector);
                return;
            }
        }
        
        log.warn("未找到类型为 {} 的可用数据源", sourceType);
    }

    /**
     * 获取所有采集器
     */
    public List<DataSourceCollector> getAllCollectors() {
        return new ArrayList<>(collectors);
    }

    /**
     * 更新数据源最后采集时间
     */
    private void updateDataSourceCollectTime(String sourceType) {
        try {
            List<DataSource> dataSources = dataSourceRepository.findByType(sourceType);
            for (DataSource dataSource : dataSources) {
                dataSource.setLastCollectTime(LocalDateTime.now());
                dataSource.setUpdatedAt(LocalDateTime.now());
                dataSourceRepository.save(dataSource);
            }
        } catch (Exception e) {
            log.error("更新数据源采集时间失败", e);
        }
    }

    /**
     * 检查信号是否已存在
     */
    private boolean isSignalExists(InfoSignal signal) {
        try {
            // 根据标题和来源检查是否已存在
            List<InfoSignal> existing = infoSignalRepository.findByTitleAndSource(
                signal.getTitle(), 
                signal.getSource()
            );
            return !existing.isEmpty();
        } catch (Exception e) {
            log.error("检查信号是否存在时出错", e);
            return false;
        }
    }
}
