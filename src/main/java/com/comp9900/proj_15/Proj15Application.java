package com.comp9900.proj_15;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.comp9900.proj_15.mapper")
@EnableScheduling
public class Proj15Application {

    public static void main(String[] args) {
        SpringApplication.run(Proj15Application.class, args);
    }

}