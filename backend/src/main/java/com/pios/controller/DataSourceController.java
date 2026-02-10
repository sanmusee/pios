package com.pios.controller;

import com.pios.common.result.Result;
import com.pios.entity.DataSource;
import com.pios.service.DataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源管理控制器
 */
@Tag(name = "数据源管理")
@RestController
@RequestMapping("/v1/data-sources")
@RequiredArgsConstructor
public class DataSourceController {

    private final DataSourceService dataSourceService;

    @Operation(summary = "获取所有数据源")
    @GetMapping
    public Result<List<DataSource>> list() {
        return Result.success(dataSourceService.findAll());
    }

    @Operation(summary = "根据ID获取数据源")
    @GetMapping("/{id}")
    public Result<DataSource> getById(@PathVariable Long id) {
        return dataSourceService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "数据源不存在"));
    }

    @Operation(summary = "创建数据源")
    @PostMapping
    public Result<DataSource> create(@RequestBody DataSource dataSource) {
        return Result.success(dataSourceService.save(dataSource));
    }

    @Operation(summary = "更新数据源")
    @PutMapping("/{id}")
    public Result<DataSource> update(@PathVariable Long id, @RequestBody DataSource dataSource) {
        dataSource.setId(id);
        return Result.success(dataSourceService.save(dataSource));
    }

    @Operation(summary = "删除数据源")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        dataSourceService.deleteById(id);
        return Result.success(null);
    }

    @Operation(summary = "根据类型获取数据源")
    @GetMapping("/type/{type}")
    public Result<List<DataSource>> getByType(@PathVariable String type) {
        return Result.success(dataSourceService.findByType(type));
    }

    @Operation(summary = "手动触发指定数据源采集")
    @PostMapping("/{id}/collect")
    public Result<Void> triggerCollection(@PathVariable Long id) {
        dataSourceService.triggerCollection(id);
        return Result.success(null);
    }

    @Operation(summary = "手动触发所有数据源采集")
    @PostMapping("/collect-all")
    public Result<Void> triggerAllCollection() {
        dataSourceService.triggerAllCollection();
        return Result.success(null);
    }
}
