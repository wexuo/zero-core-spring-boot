/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.controller;


import com.zero.boot.code.config.GeneratorConfig;
import com.zero.boot.core.data.EpsData;
import com.zero.boot.core.service.CoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
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
    public List<String> updateGeneratorConfig(@RequestBody final List<GeneratorConfig> configs) throws IOException {
        return coreService.updateGeneratorConfig(configs);
    }

    @GetMapping("/generate")
    public Map<String, List<Path>> generate(final String tableName) throws Exception {
        return coreService.generate(tableName);
    }
}
