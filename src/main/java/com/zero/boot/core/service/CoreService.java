/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.service;

import com.zero.boot.code.BuilderFactory;
import com.zero.boot.code.config.GeneratorConfig;
import com.zero.boot.core.data.EpsData;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> updateGeneratorConfig(final List<GeneratorConfig> configs) throws IOException {
        if (CollectionUtils.isEmpty(configs)) {
            return Collections.emptyList();
        }
        return configs.stream().map(config -> {
            try {
                return BuilderFactory.updateGeneratorConfig(config);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public void generate(final String tableName) throws Exception {
        BuilderFactory.build(tableName, entityManager);
    }
}
