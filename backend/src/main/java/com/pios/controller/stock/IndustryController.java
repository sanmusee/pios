package com.pios.controller.stock;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pios.common.result.Result;
import com.pios.entity.stock.Industry;
import com.pios.entity.stock.IndustryFinancialSummary;
import com.pios.entity.stock.IndustryScoreMonthly;
import com.pios.service.stock.IndustryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "行业分析")
@RestController
@RequestMapping("/v1/industry")
@RequiredArgsConstructor
public class IndustryController {

    private final IndustryService industryService;

    @Operation(summary = "获取行业列表")
    @GetMapping("/list")
    public Result<List<Industry>> list(
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) Integer parentId) {
        
        QueryWrapper<Industry> wrapper = new QueryWrapper<>();
        if (level != null) {
            wrapper.eq("level", level);
        }
        if (parentId != null) {
            wrapper.eq("parent_id", parentId);
        }
        
        return Result.success(industryService.list(wrapper));
    }

    @Operation(summary = "获取行业详情")
    @GetMapping("/{id}")
    public Result<Industry> getById(@PathVariable Integer id) {
        return Result.success(industryService.getById(id));
    }

    @Operation(summary = "获取行业财务数据")
    @GetMapping("/{id}/financial")
    public Result<List<IndustryFinancialSummary>> getFinancial(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "8") Integer limit) {
        return Result.success(industryService.getFinancialData(id, limit));
    }

    @Operation(summary = "获取行业评分")
    @GetMapping("/{id}/score")
    public Result<IndustryScoreMonthly> getScore(@PathVariable Integer id) {
        return Result.success(industryService.getLatestScore(id));
    }

    @Operation(summary = "获取热门行业")
    @GetMapping("/hot")
    public Result<List<IndustryScoreMonthly>> getHotIndustries(
            @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(industryService.getTopIndustries(limit));
    }
}
