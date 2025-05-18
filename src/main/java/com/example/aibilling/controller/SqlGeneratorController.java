package com.example.aibilling.controller;

import com.example.aibilling.service.SqlGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sql")
@RequiredArgsConstructor
public class SqlGeneratorController {

    private final SqlGeneratorService sqlGeneratorService;

    @PostMapping("/generate")
    public String generateSql(@RequestBody final String rule) {
        return sqlGeneratorService.generateAndValidateSql(rule);
    }
}
