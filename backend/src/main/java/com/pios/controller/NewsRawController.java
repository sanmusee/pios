package com.pios.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pios.common.result.Result;
import com.pios.dto.NewsRawDTO;
import com.pios.entity.NewsRaw;
import com.pios.mapper.NewsRawMapper;
import com.pios.service.NewsRawService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "原始新闻管理")
@RestController
@RequestMapping("/v1/news-raw")
@RequiredArgsConstructor
public class NewsRawController {

    private final NewsRawService newsRawService;
    private final NewsRawMapper newsRawMapper;

    @Operation(summary = "获取所有新闻")
    @GetMapping
    public Result<List<NewsRawDTO>> list() {
        QueryWrapper<NewsRaw> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("publish_time");
        List<NewsRaw> list = newsRawMapper.selectList(wrapper);
        List<NewsRawDTO> dtoList = list.stream()
                .map(NewsRawDTO::fromEntity)
                .collect(Collectors.toList());
        return Result.success(dtoList);
    }

    @Operation(summary = "根据ID获取新闻")
    @GetMapping("/{id}")
    public Result<NewsRaw> getById(@PathVariable Long id) {
        return newsRawService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "新闻不存在"));
    }

    @Operation(summary = "创建新闻")
    @PostMapping
    public Result<NewsRaw> create(@RequestBody NewsRaw newsRaw) {
        if (newsRawService.existsByUrl(newsRaw.getUrl())) {
            return Result.error(409, "该URL已存在");
        }
        return Result.success(newsRawService.save(newsRaw));
    }

    @Operation(summary = "更新新闻")
    @PutMapping("/{id}")
    public Result<NewsRaw> update(@PathVariable Long id, @RequestBody NewsRaw newsRaw) {
        newsRaw.setId(id);
        return Result.success(newsRawService.save(newsRaw));
    }

    @Operation(summary = "删除新闻")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        newsRawService.deleteById(id);
        return Result.success(null);
    }

    @Operation(summary = "根据来源获取新闻")
    @GetMapping("/source/{source}")
    public Result<List<NewsRaw>> getBySource(
            @PathVariable String source,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "20") Integer limit) {
        return Result.success(newsRawService.findBySource(source, limit));
    }

    @Operation(summary = "根据分类获取新闻")
    @GetMapping("/category/{category}")
    public Result<List<NewsRaw>> getByCategory(
            @PathVariable String category,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "20") Integer limit) {
        return Result.success(newsRawService.findByCategory(category, limit));
    }

    @Operation(summary = "根据时间范围获取新闻")
    @GetMapping("/time-range")
    public Result<List<NewsRaw>> getByTimeRange(
            @Parameter(description = "开始时间") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "结束时间") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(newsRawService.findByTimeRange(startTime, endTime));
    }

    @Operation(summary = "获取今日新闻数量")
    @GetMapping("/count/today")
    public Result<Long> countToday() {
        return Result.success(newsRawService.countToday());
    }

    @Operation(summary = "检查URL是否存在")
    @GetMapping("/exists")
    public Result<Boolean> existsByUrl(@RequestParam String url) {
        return Result.success(newsRawService.existsByUrl(url));
    }
}
