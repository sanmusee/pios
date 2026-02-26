package com.pios.service.stock;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pios.entity.stock.TradeSignal;

import java.util.List;

public interface TradeSignalService extends IService<TradeSignal> {
    
    List<TradeSignal> getSignals(String code, String signalType, Integer limit);
    
    List<TradeSignal> getLatestBuySignals(Integer limit);
}
