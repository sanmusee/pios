package com.pios.controller;

import com.pios.common.result.Result;
import com.pios.entity.InfoSignal;
import com.pios.service.InfoSignalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "信息信号管理")
@RestController
@RequestMapping("/v1/info-signals")
@RequiredArgsConstructor
public class InfoSignalController {

    private final InfoSignalService infoSignalService;

    @Operation(summary = "获取所有信号")
    @GetMapping
    public Result<List<InfoSignal>> list() {
        return Result.success(infoSignalService.findAll());
    }

    @Operation(summary = "根据ID获取信号")
    @GetMapping("/{id}")
    public Result<InfoSignal> getById(@PathVariable Long id) {
        return infoSignalService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "信号不存在"));
    }

    @Operation(summary = "创建信号")
    @PostMapping
    public Result<InfoSignal> create(@RequestBody InfoSignal signal) {
        return Result.success(infoSignalService.save(signal));
    }

    @Operation(summary = "更新信号")
    @PutMapping("/{id}")
    public Result<InfoSignal> update(@PathVariable Long id, @RequestBody InfoSignal signal) {
        signal.setId(id);
        return Result.success(infoSignalService.save(signal));
    }

    @Operation(summary = "删除信号")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        infoSignalService.deleteById(id);
        return Result.success(null);
    }
    
    @Operation(summary = "根据分类获取信号")
    @GetMapping("/category/{category}")
    public Result<List<InfoSignal>> getByCategory(@PathVariable String category) {
        return Result.success(infoSignalService.findByCategory(category));
    }
    
    @Operation(summary = "根据等级获取信号")
    @GetMapping("/level/{level}")
    public Result<List<InfoSignal>> getByLevel(@PathVariable Integer level) {
        return Result.success(infoSignalService.findByLevel(level));
    }
}
