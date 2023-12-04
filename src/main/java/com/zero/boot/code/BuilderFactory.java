package com.zero.boot.code;


import com.zero.boot.code.data.TemplateData;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BuilderFactory {

    public static Path build(final TemplateData data) throws Exception {
        final File backendFileDir = ResourceUtils.getFile("classpath:templates");
        BuilderFactory.build(backendFileDir, "Controller.ftl", "controller", data.getEntityName(), "Controller.java", data);
        BuilderFactory.build(backendFileDir, "Query.ftl", "query", data.getEntityName(), "Query.java", data);
        BuilderFactory.build(backendFileDir, "Repository.ftl", "repository", data.getEntityName(), "Repository.java", data);
        BuilderFactory.build(backendFileDir, "Service.ftl", "service", data.getEntityName(), "Service.java", data);

        final File frontFileDir = ResourceUtils.getFile("classpath:templates");
        BuilderFactory.build(frontFileDir, "index.ftl", "", "index", ".vue", data);
        return Paths.get(System.getProperty("user.home"), ".zero", "template");
    }

    public static void build(final File directory, final String filename, final String model, final String prefix, final String suffix,
                             final TemplateData data) throws Exception {
        final Template template = load(directory, filename);
        final String pack = data.getPack();

        final StringBuilder builder = new StringBuilder(".zero");
        builder.append(" ").append(pack.replace(".", " "));
        if (!StringUtils.isEmpty(model)) {
            builder.append(" ").append(model);
        }
        final String[] dirs = builder.toString().split(" ");

        final Path path = Paths.get(System.getProperty("user.home"), dirs);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        final Path resolve = path.resolve(prefix + suffix);
        Files.deleteIfExists(resolve);
        final Path filePath = Files.createFile(resolve);
        final File file = filePath.toFile();
        BuilderFactory.process(template, data, file);
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

    private static File getOutFile(final String dir, final String... more) throws IOException {
        final Path path = Paths.get(dir, more);
        Files.deleteIfExists(path);
        final Path filePath = Files.createFile(path);
        return filePath.toFile();
    }
}