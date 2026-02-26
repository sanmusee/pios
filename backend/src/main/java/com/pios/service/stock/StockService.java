package com.pios.service.stock;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pios.entity.stock.StockBasic;
import com.pios.entity.stock.StockFinancialRaw;
import com.pios.entity.stock.StockPriceDaily;
import com.pios.entity.stock.StockScoreMonthly;

import java.util.List;

public interface StockService extends IService<StockBasic> {
    
    List<StockFinancialRaw> getFinancialData(String code, Integer limit);
    
    List<StockPriceDaily> getPriceData(String code, Integer days);
    
    StockScoreMonthly getLatestScore(String code);
    
    List<StockScoreMonthly> getTopStocks(Integer limit);
}
