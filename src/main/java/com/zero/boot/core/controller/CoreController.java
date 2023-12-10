/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.controller;


import com.zero.boot.code.config.GeneratorConfig;
import com.zero.boot.core.annotation.ResponseResult;
import com.zero.boot.core.data.EpsData;
import com.zero.boot.core.service.CoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@ResponseResult
public class CoreController {

    @Resource
    private CoreService coreService;

    @GetMapping("/eps")
    public EpsData getEps() {
        return coreService.eps();
    }

    @GetMapping("/generate/config")
    public List<GeneratorConfig> getGeneratorConfig() {
        return coreService.getGeneratorConfig();
    }

    @PostMapping("/generate/config")
    public String updateGeneratorConfig(final GeneratorConfig config) throws IOException {
        return coreService.updateGeneratorConfig(config);
    }

    @GetMapping("/generate")
    public void generate(final String tableName) throws Exception {
        coreService.generate(tableName);
    }
}
