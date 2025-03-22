package com.comp9900.proj_15.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}