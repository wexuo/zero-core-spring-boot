/*
 * Copyright (c) 2023-2024 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.service;

import com.zero.boot.code.BuilderFactory;
import com.zero.boot.code.TemplateBuilder;
import com.zero.boot.code.config.GeneratorConfig;
import com.zero.boot.code.data.TableData;
import com.zero.boot.core.annotation.Module;
import com.zero.boot.core.data.EpsData;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoreService {

    @Resource
    private EntityManager entityManager;

    @Resource
    private RequestMappingHandlerMapping mapping;

    @Resource
    private ApplicationContext context;

    public Map<String, List<EpsData>> eps() {
        final List<TableData> tables = TemplateBuilder.getTableData(entityManager);
        final Map<Class<?>, List<EpsData.EpsApi>> clazzApisMap = new HashMap<>();
        mapping.getHandlerMethods().forEach((info, handlerMethod) -> {
            // 获取请求方法
            final Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            // 获取请求路径
            final Set<String> patterns = info.getPatternsCondition().getPatterns();
            final List<EpsData.EpsApi> apis = new ArrayList<>();
            for (final String pattern : patterns) {
                for (final RequestMethod method : methods) {
                    final EpsData.EpsApi api = new EpsData.EpsApi();
                    api.setMethod(method.name().toLowerCase());
                    api.setPath(pattern);
                    apis.add(api);
                }
            }
            // 获取请求路径对应的类
            final Class<?> clazz = handlerMethod.getBeanType();
            clazzApisMap.computeIfAbsent(clazz, k -> new ArrayList<>()).addAll(apis);
        });

        final Map<? extends Class<?>, TableData> tableDataMap = tables.stream()
                .collect(Collectors.toMap(TableData::getClazz, e -> e, (e1, e2) -> e1));
        return clazzApisMap.entrySet().stream().map((entry) -> {
            final Class<?> clazz = entry.getKey();
            final List<EpsData.EpsApi> apis = entry.getValue();
            final Module module = clazz.getAnnotation(Module.class);
            if (Objects.isNull(module) || CollectionUtils.isEmpty(apis)) {
                return null;
            }
            final EpsData eps = new EpsData();
            eps.setModule(module.name());
            eps.setName(module.desc());
            final Class<?> entity = module.entity();
            final TableData tableData = tableDataMap.get(entity);
            if (Objects.nonNull(tableData)) {
                final List<EpsData.EpsColumn> columns = tableData.getProperties().stream().map(property -> {
                    final EpsData.EpsColumn column = new EpsData.EpsColumn();
                    column.setPropertyName(property.getProp());
                    column.setType(property.getType());
                    column.setComment(property.getLabel());
                    column.setNullable(property.getNullable());
                    return column;
                }).collect(Collectors.toList());
                eps.setColumns(columns);
            }
            eps.setApi(apis);
            return eps;
        }).filter(Objects::nonNull).collect(Collectors.groupingBy(EpsData::getModule));
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

    public Map<String, List<Path>> generate(final String tableName) throws Exception {
        if (Objects.nonNull(tableName)) {
            final List<Path> paths = BuilderFactory.build(tableName, entityManager);
            return new HashMap<String, List<Path>>() {{
                put(tableName, paths);
            }};
        }
        return BuilderFactory.build(entityManager);
    }
}
