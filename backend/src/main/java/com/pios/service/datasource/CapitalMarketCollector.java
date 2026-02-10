package com.pios.service.datasource;

import com.pios.entity.InfoSignal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 资本市场数据采集器
 * 采集股票指数、国债收益率、大宗商品、贵金属等数据
 */
@Slf4j
@Component
public class CapitalMarketCollector implements DataSourceCollector {

    @Override
    public String getSourceName() {
        return "资本市场数据";
    }

    @Override
    public String getSourceType() {
        return "CAPITAL_MARKET";
    }

    @Override
    public List<InfoSignal> collect() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            // 采集A股指数
            signals.addAll(collectAStockIndex());
            
            // 采集港股指数
            signals.addAll(collectHKStockIndex());
            
            // 采集美股指数
            signals.addAll(collectUSStockIndex());
            
            // 采集国债收益率
            signals.addAll(collectBondYield());
            
            // 采集大宗商品
            signals.addAll(collectCommodities());
            
            // 采集贵金属
            signals.addAll(collectPreciousMetals());
            
        } catch (Exception e) {
            log.error("采集资本市场数据失败", e);
        }
        
        return signals;
    }

    /**
     * 采集A股指数
     */
    private List<InfoSignal> collectAStockIndex() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("A股市场指数更新");
            signal.setContent("上证指数、深证成指、创业板指等主要指数变动");
            signal.setSource(getSourceName());
            signal.setCategory("资本市场");
            signal.setLevel(1); // 情绪级波动
            signal.setTags("A股,上证指数,深证成指,创业板指");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到A股指数数据");
            
        } catch (Exception e) {
            log.error("采集A股指数失败", e);
        }
        
        return signals;
    }

    /**
     * 采集港股指数
     */
    private List<InfoSignal> collectHKStockIndex() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("港股市场指数更新");
            signal.setContent("恒生指数、恒生科技指数变动");
            signal.setSource(getSourceName());
            signal.setCategory("资本市场");
            signal.setLevel(1);
            signal.setTags("港股,恒生指数,恒生科技");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到港股指数数据");
            
        } catch (Exception e) {
            log.error("采集港股指数失败", e);
        }
        
        return signals;
    }

    /**
     * 采集美股指数
     */
    private List<InfoSignal> collectUSStockIndex() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("美股市场指数更新");
            signal.setContent("道琼斯、纳斯达克、标普500指数变动");
            signal.setSource(getSourceName());
            signal.setCategory("资本市场");
            signal.setLevel(2); // 中期趋势
            signal.setTags("美股,道琼斯,纳斯达克,标普500");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到美股指数数据");
            
        } catch (Exception e) {
            log.error("采集美股指数失败", e);
        }
        
        return signals;
    }

    /**
     * 采集国债收益率
     */
    private List<InfoSignal> collectBondYield() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("国债收益率变化");
            signal.setContent("中美国债收益率曲线变动，反映资金成本和市场预期");
            signal.setSource(getSourceName());
            signal.setCategory("资本市场");
            signal.setLevel(2);
            signal.setTags("国债,收益率,利率,债券");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到国债收益率数据");
            
        } catch (Exception e) {
            log.error("采集国债收益率失败", e);
        }
        
        return signals;
    }

    /**
     * 采集大宗商品
     */
    private List<InfoSignal> collectCommodities() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("大宗商品价格变动");
            signal.setContent("原油、铜、铁矿石等主要大宗商品价格变化");
            signal.setSource(getSourceName());
            signal.setCategory("资本市场");
            signal.setLevel(2);
            signal.setTags("大宗商品,原油,铜,铁矿石");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到大宗商品数据");
            
        } catch (Exception e) {
            log.error("采集大宗商品失败", e);
        }
        
        return signals;
    }

    /**
     * 采集贵金属
     */
    private List<InfoSignal> collectPreciousMetals() {
        List<InfoSignal> signals = new ArrayList<>();
        
        try {
            InfoSignal signal = new InfoSignal();
            signal.setTitle("贵金属价格变动");
            signal.setContent("黄金、白银价格变化，反映避险需求");
            signal.setSource(getSourceName());
            signal.setCategory("资本市场");
            signal.setLevel(2);
            signal.setTags("贵金属,黄金,白银,避险");
            signal.setSignalTime(LocalDateTime.now());
            
            signals.add(signal);
            log.info("采集到贵金属数据");
            
        } catch (Exception e) {
            log.error("采集贵金属失败", e);
        }
        
        return signals;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getCollectInterval() {
        return 30; // 每30分钟采集一次
    }
}
