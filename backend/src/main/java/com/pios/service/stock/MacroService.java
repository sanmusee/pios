package com.pios.service.stock;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pios.entity.stock.MacroData;

import java.util.List;

public interface MacroService extends IService<MacroData> {
    
    List<String> getAllIndicators();
    
    List<MacroData> getMacroData(String indicatorCode, Integer months);
}
