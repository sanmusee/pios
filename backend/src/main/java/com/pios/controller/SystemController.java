package com.pios.controller;

import com.pios.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "系统管理")
@RestController
@RequestMapping("/v1/system")
public class SystemController {

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public Result<Map<String, String>> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "PIOS Backend");
        health.put("version", "1.0.0");
        return Result.success(health);
    }

    @Operation(summary = "获取系统信息")
    @GetMapping("/info")
    public Result<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "PIOS");
        info.put("description", "Personal Intelligence Operating System");
        info.put("layers", new String[]{
            "Raw Layer - 信息采集",
            "Filter Layer - 信号分级",
            "Correlation Layer - 关联分析",
            "Inference Layer - 推演系统",
            "Report Layer - 日报中心"
        });
        return Result.success(info);
    }
}
