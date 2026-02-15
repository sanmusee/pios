package com.pios.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.pios.service.python.PythonExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/python")
@RequiredArgsConstructor
public class PythonController {

    private final PythonExecutorService pythonExecutorService;

    @GetMapping("/macro/{dataType}")
    public ResponseEntity<JsonNode> fetchMacroData(@PathVariable String dataType) {
        JsonNode result = pythonExecutorService.fetchMacroData(dataType);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/execute/{fetcher}/{method}")
    public ResponseEntity<JsonNode> executeFetcher(
            @PathVariable String fetcher,
            @PathVariable String method,
            @RequestBody(required = false) String params) {
        JsonNode result = pythonExecutorService.executeFetcher(
            fetcher,
            method,
            params != null ? params : "{}"
        );
        return ResponseEntity.ok(result);
    }
}
