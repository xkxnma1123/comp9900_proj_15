package com.comp9900.proj_15;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/backend_db?useSSL=false&serverTimezone=UTC&characterEncoding=utf-8";
        String username = "root";
        String password = "ab20011123";

        // 项目路径
        String projectPath = System.getProperty("user.dir");

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("root") // 设置作者
                            .outputDir(projectPath + "/src/main/java") // 指定输出目录
                            .commentDate("yyyy-MM-dd")
                            .fileOverride(); // 覆盖已生成文件
                })
                .packageConfig(builder -> {
                    builder.parent("com.comp9900.proj_15") // 设置父包名
                            .moduleName("") // 设置父包模块名，可以为空
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    projectPath + "/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("Event", "Friends", "Message", "User", "User_Evnet") // 设置需要生成的表名，可以写多个
                            // 实体策略配置
                            .entityBuilder()
                            .enableLombok() // 开启Lombok
                            .logicDeleteColumnName("deleted") // 逻辑删除字段
                            .enableTableFieldAnnotation() // 开启生成实体时生成表字段注解

                            // Mapper策略配置
                            .mapperBuilder()
                            .enableMapperAnnotation() // 开启 @Mapper 注解

                            // Service策略配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")

                            // Controller策略配置
                            .controllerBuilder()
                            .enableRestStyle(); // 开启生成 @RestController 控制器
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}