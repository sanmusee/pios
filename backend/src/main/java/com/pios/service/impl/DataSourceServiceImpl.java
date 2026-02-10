package com.pios.service.impl;

import com.pios.entity.DataSource;
import com.pios.repository.DataSourceRepository;
import com.pios.service.DataSourceService;
import com.pios.service.datasource.DataCollectionManager;
import com.pios.service.datasource.DataSourceCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 数据源服务实现
 */
@Slf4j
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceRepository dataSourceRepository;
    
    @Autowired
    private DataCollectionManager dataCollectionManager;

    @Override
    public DataSource save(DataSource dataSource) {
        return dataSourceRepository.save(dataSource);
    }

    @Override
    public Optional<DataSource> findById(Long id) {
        return dataSourceRepository.findById(id);
    }

    @Override
    public List<DataSource> findAll() {
        return dataSourceRepository.findAll();
    }

    @Override
    public List<DataSource> findByType(String type) {
        return dataSourceRepository.findByType(type);
    }

    @Override
    public void deleteById(Long id) {
        dataSourceRepository.deleteById(id);
    }

    @Override
    public void triggerCollection(Long id) {
        Optional<DataSource> dataSourceOpt = findById(id);
        if (dataSourceOpt.isPresent()) {
            DataSource dataSource = dataSourceOpt.get();
            String type = dataSource.getType();
            
            // 查找对应的采集器
            for (DataSourceCollector collector : dataCollectionManager.getAllCollectors()) {
                if (collector.getSourceType().equalsIgnoreCase(type)) {
                    dataCollectionManager.collectFromSource(collector);
                    return;
                }
            }
            
            log.warn("未找到数据源 {} 对应的采集器", dataSource.getName());
        } else {
            log.warn("数据源 {} 不存在", id);
        }
    }

    @Override
    public void triggerAllCollection() {
        dataCollectionManager.collectAll();
    }
}
