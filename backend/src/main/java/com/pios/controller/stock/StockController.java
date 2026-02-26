package com.pios.controller.stock;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pios.common.result.Result;
import com.pios.entity.stock.StockBasic;
import com.pios.entity.stock.StockFinancialRaw;
import com.pios.entity.stock.StockPriceDaily;
import com.pios.entity.stock.StockScoreMonthly;
import com.pios.service.stock.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 股票分析控制器
 */
@Tag(name = "股票分析")
@RestController
@RequestMapping("/v1/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @Operation(summary = "获取股票列表")
    @GetMapping("/list")
    public Result<Page<StockBasic>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String exchange,
            @RequestParam(required = false) Integer industryId) {
        
        Page<StockBasic> pageParam = new Page<>(page, size);
        QueryWrapper<StockBasic> wrapper = new QueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("name", keyword).or().like("code", keyword);
        }
        if (exchange != null && !exchange.isEmpty()) {
            wrapper.eq("exchange", exchange);
        }
        if (industryId != null) {
            wrapper.eq("industry_id", industryId);
        }
        
        return Result.success(stockService.page(pageParam, wrapper));
    }

    @Operation(summary = "获取股票详情")
    @GetMapping("/{code}")
    public Result<StockBasic> getByCode(@PathVariable String code) {
        return Result.success(stockService.getById(code));
    }

    @Operation(summary = "获取股票财务数据")
    @GetMapping("/{code}/financial")
    public Result<List<StockFinancialRaw>> getFinancial(
            @PathVariable String code,
            @RequestParam(defaultValue = "8") Integer limit) {
        return Result.success(stockService.getFinancialData(code, limit));
    }

    @Operation(summary = "获取股票行情数据")
    @GetMapping("/{code}/price")
    public Result<List<StockPriceDaily>> getPrice(
            @PathVariable String code,
            @RequestParam(defaultValue = "60") Integer days) {
        return Result.success(stockService.getPriceData(code, days));
    }

    @Operation(summary = "获取股票评分")
    @GetMapping("/{code}/score")
    public Result<StockScoreMonthly> getScore(@PathVariable String code) {
        return Result.success(stockService.getLatestScore(code));
    }

    @Operation(summary = "获取推荐股票列表")
    @GetMapping("/recommendations")
    public Result<List<StockScoreMonthly>> getRecommendations(
            @RequestParam(defaultValue = "20") Integer limit) {
        return Result.success(stockService.getTopStocks(limit));
    }
}
