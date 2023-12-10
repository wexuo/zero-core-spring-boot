/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.service;

import com.zero.boot.code.BuilderFactory;
import com.zero.boot.code.config.GeneratorConfig;
import com.zero.boot.core.data.EpsData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

@Service
public class CoreService {

    @Resource
    private EntityManager entityManager;

    public EpsData eps() {
        // 获取所有的 JPA Entity
        return null;
    }

    public List<GeneratorConfig> getGeneratorConfig() {
        return BuilderFactory.getTemplateData(entityManager);
    }

    public String updateGeneratorConfig(final GeneratorConfig config) throws IOException {
        return BuilderFactory.updateGeneratorConfig(config);
    }

    public void generate(final String tableName) throws Exception {
        BuilderFactory.build(tableName, entityManager);
    }
}
