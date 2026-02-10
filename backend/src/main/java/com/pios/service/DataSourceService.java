package com.pios.service;

import com.pios.entity.DataSource;
import java.util.List;
import java.util.Optional;

/**
 * 数据源服务接口
 */
public interface DataSourceService {
    
    /**
     * 保存数据源
     */
    DataSource save(DataSource dataSource);
    
    /**
     * 根据ID查询
     */
    Optional<DataSource> findById(Long id);
    
    /**
     * 查询所有数据源
     */
    List<DataSource> findAll();
    
    /**
     * 根据类型查询
     */
    List<DataSource> findByType(String type);
    
    /**
     * 删除数据源
     */
    void deleteById(Long id);
    
    /**
     * 手动触发数据采集
     */
    void triggerCollection(Long id);
    
    /**
     * 手动触发所有数据源采集
     */
    void triggerAllCollection();
}
