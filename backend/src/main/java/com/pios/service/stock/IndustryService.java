package com.pios.service.stock;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pios.entity.stock.Industry;
import com.pios.entity.stock.IndustryFinancialSummary;
import com.pios.entity.stock.IndustryScoreMonthly;

import java.util.List;

public interface IndustryService extends IService<Industry> {
    
    List<IndustryFinancialSummary> getFinancialData(Integer industryId, Integer limit);
    
    IndustryScoreMonthly getLatestScore(Integer industryId);
    
    List<IndustryScoreMonthly> getTopIndustries(Integer limit);
}
