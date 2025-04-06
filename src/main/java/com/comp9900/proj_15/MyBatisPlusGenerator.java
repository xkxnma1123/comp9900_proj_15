package com.comp9900.proj_15;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

/**
 * MyBatis-Plus Code Generator
 * Run the main method of this class to automatically generate related code
 */
public class MyBatisPlusGenerator {

    // Database connection configuration
    private static final String URL = "jdbc:mysql://localhost:3306/backend_db?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "ab20011123";

    // Package name and module name
    private static final String PACKAGE_NAME = "com.comp9900.proj_15";
    private static final String MODULE_NAME = "";

    // Author
    private static final String AUTHOR = "comp9900_proj15";

    // Table prefix (optional)
    private static final String[] TABLE_PREFIX = {};

    // Output directory
    private static final String OUTPUT_DIR = System.getProperty("user.dir") + "/src/main/java";

    // XML file output directory
    private static final String XML_OUTPUT_DIR = System.getProperty("user.dir") + "/src/main/resources/mapper";

    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                // Global configuration
                .globalConfig(builder -> {
                    builder.author(AUTHOR)
                            .enableSwagger()
                            .fileOverride()
                            .disableOpenDir()
                            .outputDir(OUTPUT_DIR);
                })
                // Package configuration
                .packageConfig(builder -> {
                    builder.parent(PACKAGE_NAME)
                            .moduleName(MODULE_NAME)
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, XML_OUTPUT_DIR));
                })
                // Strategy configuration
                .strategyConfig(builder -> {
                    builder.addInclude(
                                    "User",
                                    "Event",
                                    "Friends",
                                    "Message",
                                    "User_Event"
                            )
                            .addTablePrefix(TABLE_PREFIX)

                            // Entity strategy configuration
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .enableRemoveIsPrefix()
                            .enableColumnConstant()
                            .enableActiveRecord()

                            // Mapper strategy configuration
                            .mapperBuilder()
                            .enableMapperAnnotation()
                            .enableBaseResultMap()
                            .enableBaseColumnList()

                            // Service strategy configuration
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")

                            // Controller strategy configuration
                            .controllerBuilder()
                            .enableRestStyle();
                })
                // Custom configuration: Handle enum type fields
                .injectionConfig(builder -> {
                    builder.beforeOutputFile((tableInfo, objectMap) -> {
                        System.out.println("Generating table: " + tableInfo.getEntityName());
                    });
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
