package com.pios.service.datasource;

import com.pios.entity.InfoSignal;
import java.util.List;

/**
 * 数据源采集接口
 * 所有具体数据源采集器都需要实现此接口
 */
public interface DataSourceCollector {
    
    /**
     * 获取数据源名称
     */
    String getSourceName();
    
    /**
     * 获取数据源类型
     */
    String getSourceType();
    
    /**
     * 采集数据
     * @return 采集到的信息信号列表
     */
    List<InfoSignal> collect();
    
    /**
     * 检查数据源是否可用
     */
    boolean isAvailable();
    
    /**
     * 获取采集间隔（分钟）
     */
    int getCollectInterval();
}
