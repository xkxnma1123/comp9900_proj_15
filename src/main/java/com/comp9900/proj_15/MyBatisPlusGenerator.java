package com.comp9900.proj_15;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

/**
 * MyBatis-Plus代码生成器
 * 运行此类的main方法即可自动生成相关代码
 */
public class MyBatisPlusGenerator {

    // 数据库连接配置
    private static final String URL = "jdbc:mysql://localhost:3306/backend_db?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "ab20011123";

    // 包名和模块名
    private static final String PACKAGE_NAME = "com.comp9900.proj_15";
    private static final String MODULE_NAME = "";

    // 作者
    private static final String AUTHOR = "comp9900_proj15";

    // 表前缀(可选)
    private static final String[] TABLE_PREFIX = {};

    // 输出目录
    private static final String OUTPUT_DIR = System.getProperty("user.dir") + "/src/main/java";

    // XML文件输出目录
    private static final String XML_OUTPUT_DIR = System.getProperty("user.dir") + "/src/main/resources/mapper";

    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已有文件
                            .disableOpenDir() // 禁止打开输出目录
                            .outputDir(OUTPUT_DIR); // 指定输出目录
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent(PACKAGE_NAME) // 设置父包名
                            .moduleName(MODULE_NAME) // 设置父包模块名
                            .entity("entity") // 实体类包名
                            .service("service") // 服务类包名
                            .serviceImpl("service.impl") // 服务实现类包名
                            .mapper("mapper") // Mapper包名
                            .controller("controller") // Controller包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, XML_OUTPUT_DIR)); // 设置XML文件输出目录
                })
                // 策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(
                                    "User",
                                    "Event",
                                    "Friends",
                                    "Message",
                                    "User_Event"
                            ) // 设置需要生成的表名
                            .addTablePrefix(TABLE_PREFIX) // 设置过滤表前缀

                            // 实体策略配置
                            .entityBuilder()
                            .enableLombok() // 开启 lombok 模型
                            .enableTableFieldAnnotation() // 开启字段注解
                            .enableRemoveIsPrefix() // 开启 boolean 类型字段移除is前缀
                            .enableColumnConstant() // 开启生成字段常量
                            .enableActiveRecord() // 开启 ActiveRecord 模型

                            // Mapper策略配置
                            .mapperBuilder()
                            .enableMapperAnnotation() // 开启 @Mapper 注解
                            .enableBaseResultMap() // 启用 BaseResultMap 生成
                            .enableBaseColumnList() // 启用 BaseColumnList

                            // Service策略配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService") // 设置 service 接口文件名称格式
                            .formatServiceImplFileName("%sServiceImpl") // 设置 service 实现类文件名称格式

                            // Controller策略配置
                            .controllerBuilder()
                            .enableRestStyle(); // 开启生成 @RestController 控制器
                })
                // 自定义配置：处理枚举类型字段
                .injectionConfig(builder -> {
                    builder.beforeOutputFile((tableInfo, objectMap) -> {
                        System.out.println("正在生成表：" + tableInfo.getEntityName());
                    });
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，需要导入相关依赖
                .execute();
    }
}
