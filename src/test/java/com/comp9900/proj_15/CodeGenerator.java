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

        // projectPath 
        String projectPath = System.getProperty("user.dir");

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("root") // set author
                            .outputDir(projectPath + "/src/main/java") // specify output directory
                            .commentDate("yyyy-MM-dd")
                            .fileOverride(); // overwrite existing files
                })
                .packageConfig(builder -> {
                    builder.parent("com.comp9900.proj_15") // set parent package name
                            .moduleName("") // set module name, can be empty
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    projectPath + "/src/main/resources/mapper")); // set mapper XML generation path
                })
                .strategyConfig(builder -> {
                    builder.addInclude("Event", "Friends", "Message", "User", "User_Evnet") // set table names to generate
                            // entity strategy configuration
                            .entityBuilder()
                            .enableLombok() // lombok support
                            .logicDeleteColumnName("deleted") // define logic delete column
                            .enableTableFieldAnnotation() // open @TableField annotation

                            // mapper strategy configuration
                            .mapperBuilder()
                            .enableMapperAnnotation() // open @Mapper annotation

                            // Service strategy configuration
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")

                            // Controller strategy configuration
                            .controllerBuilder()
                            .enableRestStyle(); // open @RestController annotation
                })
                .templateEngine(new FreemarkerTemplateEngine()) // Use Freemarker engine template, the default is Velocity engine template
                .execute();
    }
}