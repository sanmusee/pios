package com.pios.controller;

import com.pios.collector.rss.Kr36RssCollector;
import com.pios.common.result.Result;
import com.pios.entity.NewsRaw;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RSS采集控制器
 */
@Tag(name = "RSS采集管理")
@RestController
@RequestMapping("/v1/collector")
@RequiredArgsConstructor
public class RssCollectorController {

    private final Kr36RssCollector kr36RssCollector;

    @Operation(summary = "采集36Kr RSS数据")
    @PostMapping("/rss/36kr")
    public Result<List<NewsRaw>> collect36Kr() {
        List<NewsRaw> newsList = kr36RssCollector.collect();
        return Result.success(newsList);
    }

    @Operation(summary = "获取采集器状态")
    @GetMapping("/rss/36kr/status")
    public Result<String> get36KrStatus() {
        return Result.success("36Kr采集器运行正常，RSS源: " + kr36RssCollector.getRssUrl());
    }
}
