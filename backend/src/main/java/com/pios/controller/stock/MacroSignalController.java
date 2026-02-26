package com.pios.controller.stock;

import com.pios.common.result.Result;
import com.pios.entity.stock.MacroData;
import com.pios.entity.stock.TradeSignal;
import com.pios.service.stock.MacroService;
import com.pios.service.stock.TradeSignalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "宏观与信号")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class MacroSignalController {

    private final MacroService macroService;
    private final TradeSignalService tradeSignalService;

    @Operation(summary = "获取宏观指标列表")
    @GetMapping("/macro/indicators")
    public Result<List<String>> getIndicators() {
        return Result.success(macroService.getAllIndicators());
    }

    @Operation(summary = "获取宏观数据")
    @GetMapping("/macro/{indicatorCode}")
    public Result<List<MacroData>> getMacroData(
            @PathVariable String indicatorCode,
            @RequestParam(defaultValue = "24") Integer months) {
        return Result.success(macroService.getMacroData(indicatorCode, months));
    }

    @Operation(summary = "获取交易信号")
    @GetMapping("/signals")
    public Result<List<TradeSignal>> getSignals(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String signalType,
            @RequestParam(defaultValue = "20") Integer limit) {
        return Result.success(tradeSignalService.getSignals(code, signalType, limit));
    }

    @Operation(summary = "获取最新买入信号")
    @GetMapping("/signals/buy")
    public Result<List<TradeSignal>> getBuySignals(
            @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(tradeSignalService.getLatestBuySignals(limit));
    }
}
