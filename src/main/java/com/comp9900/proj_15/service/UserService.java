package com.comp9900.proj_15.service;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 用户注册
     */
    Map<String, Object> register(String name, String email, String levelOfStudy, String password);
    
    /**
     * 用户登录
     */
    Map<String, Object> login(String email, String password);
}