/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.code;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.boot.code.config.GeneratorConfig;
import com.zero.boot.code.data.TableData;
import com.zero.boot.code.data.TemplateData;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import javax.persistence.EntityManager;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class BuilderFactory {
    private static final ObjectMapper OM = new ObjectMapper();
    private static final Map<String, GeneratorConfig> GENERATOR_CONFIG_MAP = new HashMap<>();

    private static Path CONFIG_PATH = Paths.get(System.getProperty("user.home"), ".zero", "generator", "config.json");

    static {
        final Path path = Paths.get(System.getProperty("user.home"), ".zero", "generator");
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (final IOException e) {
                log.error("init generator config dir failed", e);
            }
        }
        final Path resolve = path.resolve("config.json");
        if (Files.notExists(resolve)) {
            try {
                CONFIG_PATH = Files.createFile(resolve);
                Files.write(resolve, OM.writeValueAsBytes(GENERATOR_CONFIG_MAP));
            } catch (final IOException e) {
                log.error("init generator config file failed", e);
            }
        } else {
            try {
                final Map<String, GeneratorConfig> map = OM.readValue(resolve.toFile(), new TypeReference<Map<String, GeneratorConfig>>() {
                });
                GENERATOR_CONFIG_MAP.putAll(map);
            } catch (final IOException e) {
                log.error("init generator config failed", e);
            }

        }
    }

    public static String updateGeneratorConfig(final GeneratorConfig config) throws IOException {
        GENERATOR_CONFIG_MAP.put(config.getTableName(), config);
        Files.write(CONFIG_PATH, OM.writeValueAsBytes(config));
        return config.getTableName();
    }

    public static List<GeneratorConfig> getTemplateData(final EntityManager manager) {
        final List<TableData> tables = TemplateBuilder.getTableData(manager);
        return tables.stream().map(tableData -> {
            GeneratorConfig config = GENERATOR_CONFIG_MAP.get(tableData.getTableName());
            if (Objects.isNull(config)) {
                config = GeneratorConfig.covert(tableData);
            }
            return config;
        }).collect(Collectors.toList());
    }

    public static List<Path> build(final String tableName, final EntityManager manager) throws Exception {
        final List<TableData> tables = TemplateBuilder.getTableData(manager);
        final Optional<TableData> optional = tables.stream().filter(tableData -> tableData.getTableName().equals(tableName)).findFirst();
        if (optional.isPresent()) {
            final TableData tableData = optional.get();
            GeneratorConfig config = GENERATOR_CONFIG_MAP.get(tableData.getTableName());
            if (Objects.isNull(config)) {
                config = GeneratorConfig.covert(tableData);
            }
            return build(TemplateData.covert(tableData, config));
        }
        return Collections.emptyList();
    }

    public static List<Path> build(final TemplateData data) throws Exception {
        final File path = ResourceUtils.getFile("classpath:templates");
        final Path repository = BuilderFactory.build(path, "Repository.ftl", "repository", data.getEntityName(), "Repository.java", data);
        final Path service = BuilderFactory.build(path, "Service.ftl", "service", data.getEntityName(), "Service.java", data);
        final Path controller = BuilderFactory.build(path, "Controller.ftl", "controller", data.getEntityName(), "Controller.java", data);
        final Path query = BuilderFactory.build(path, "Query.ftl", "query", data.getEntityName(), "Query.java", data);

        final Path index = BuilderFactory.build(path, "index.ftl", "", "index", ".vue", data);
        return Arrays.asList(repository, service, controller, query, index);
    }

    public static Map<String, ByteArrayOutputStream> process(final TemplateData data) throws Exception {
        final File path = ResourceUtils.getFile("classpath:templates");
        return new HashMap<String, ByteArrayOutputStream>() {
            {
                {
                    put("Repository", BuilderFactory.process(path, "Repository.ftl", data));
                    put("Service", BuilderFactory.process(path, "Service.ftl", data));
                    put("Controller", BuilderFactory.process(path, "Controller.ftl", data));
                    put("Query", BuilderFactory.process(path, "Query.ftl", data));
                    put("Index", BuilderFactory.process(path, "index.ftl", data));
                }
            }
        };
    }

    public static ByteArrayOutputStream process(final File path, final String filename, final TemplateData data) throws IOException, TemplateException {
        final Template template = load(path, filename);
        return BuilderFactory.process(template, data);
    }

    public static Path build(final File directory, final String filename, final String model, final String prefix, final String suffix,
                             final TemplateData data) throws Exception {
        final Template template = load(directory, filename);
        final String pack = data.getPack();
        final Path path = getSystemPath(model, pack);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        final Path resolve = path.resolve(prefix + suffix);
        Files.deleteIfExists(resolve);
        final Path filePath = Files.createFile(resolve);
        final File file = filePath.toFile();
        try {
            BuilderFactory.process(template, data, file);
            if (StringUtils.isNotEmpty(model)) {
                Path target = getTargetPath(model, data.getClazz());
                target = target.resolve(prefix + suffix);
                Files.copy(filePath, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return filePath;
        } catch (final TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path getSystemPath(final String model, final String pack) throws IOException {
        final StringBuilder builder = new StringBuilder(".zero");
        builder.append(" ").append(pack.replace(".", " "));
        if (StringUtils.isNotEmpty(model)) {
            builder.append(" ").append(model);
        }
        final String[] dirs = builder.toString().split(" ");

        final Path path = Paths.get(System.getProperty("user.home"), dirs);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        return path;
    }

    private static Path getTargetPath(final String model, final Class<?> clazz) {
        String path = Objects.requireNonNull(clazz.getResource("")).getPath();
        path = path.replaceAll("/target/classes", "/src/main/java");
        final Path current = FileUtils.getFile(path).toPath();
        return Paths.get(current.getParent().toString(), model);
    }

    /**
     * @param directory 模板目录
     * @param filename  模板文件名
     * @return
     * @throws IOException
     */
    public static Template load(final File directory, final String filename) throws IOException {
        final Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDirectoryForTemplateLoading(directory);
        configuration.setDefaultEncoding("utf-8");
        return configuration.getTemplate(filename);
    }

    /**
     * @param template 模板
     * @param data     数据
     * @param file     输出文件
     * @throws IOException
     * @throws TemplateException
     */
    public static void process(final Template template, final TemplateData data, final File file) throws IOException, TemplateException {
        final Writer out = new FileWriter(file);
        template.process(data, out);
        out.close();
    }

    public static ByteArrayOutputStream process(final Template template, final TemplateData data) throws IOException, TemplateException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Writer out = new OutputStreamWriter(outputStream);
        template.process(data, out);
        out.close();
        return outputStream;
    }
}