package com.pios.service.datasource;

import com.pios.entity.InfoSignal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 宏观经济数据采集器
 * 采集CPI、M2、PMI、社融、利率等宏观数据
 */
@Slf4j
@Component
public class MacroDataCollector implements DataSourceCollector {

    private final RestTemplate restTemplate = new RestTemplate();
    
    @Override
    public String getSourceName() {
        return "宏观经济数据";
    }

    @Override
    public String getSourceType() {
        return "MACRO";
    }

    @Override
    public List<InfoSignal> collect() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            // 采集CPI数据
            signals.addAll(collectCPI());
            
            // 采集M2数据
            signals.addAll(collectM2());
            
            // 采集PMI数据
            signals.addAll(collectPMI());
            
            // 采集利率数据
            signals.addAll(collectInterestRate());
            
        } catch (Exception e) {
            log.error("采集宏观经济数据失败", e);
        }
        
        return signals;
    }

    /**
     * 采集CPI数据
     */
    private List<InfoSignal> collectCPI() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            // 使用模拟数据演示，实际应调用真实API
            InfoSignal signal = new InfoSignal();
            signal.setTitle("CPI数据更新");
            signal.setContent("最新CPI同比变化，反映通胀水平");
            signal.setSource(getSourceName());
            signal.setCategory("宏观经济");
            signal.setLevel(2); // 中期趋势变化
            signal.setTags("CPI,通胀,宏观");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到CPI数据");
            
        } catch (Exception e) {
            log.error("采集CPI数据失败", e);
        }
        
        return signals;
    }

    /**
     * 采集M2数据
     */
    private List<InfoSignal> collectM2() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("M2货币供应量更新");
            signal.setContent("M2同比增长率，反映货币宽松程度");
            signal.setSource(getSourceName());
            signal.setCategory("宏观经济");
            signal.setLevel(2);
            signal.setTags("M2,货币供应,流动性");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到M2数据");
            
        } catch (Exception e) {
            log.error("采集M2数据失败", e);
        }
        
        return signals;
    }

    /**
     * 采集PMI数据
     */
    private List<InfoSignal> collectPMI() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("PMI制造业指数更新");
            signal.setContent("制造业PMI指数，反映经济景气程度");
            signal.setSource(getSourceName());
            signal.setCategory("宏观经济");
            signal.setLevel(2);
            signal.setTags("PMI,制造业,景气指数");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到PMI数据");
            
        } catch (Exception e) {
            log.error("采集PMI数据失败", e);
        }
        
        return signals;
    }

    /**
     * 采集利率数据
     */
    private List<InfoSignal> collectInterestRate() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("央行利率政策更新");
            signal.setContent("最新利率调整，影响资金成本");
            signal.setSource(getSourceName());
            signal.setCategory("政策制度");
            signal.setLevel(3); // 结构级变化
            signal.setTags("利率,央行,货币政策");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到利率数据");
            
        } catch (Exception e) {
            log.error("采集利率数据失败", e);
        }
        
        return signals;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getCollectInterval() {
        return 60; // 每小时采集一次
    }
}
