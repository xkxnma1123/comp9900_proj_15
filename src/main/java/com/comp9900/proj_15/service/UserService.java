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
    
    /**
     * 更新用户邮箱验证状态
     */
    boolean updateEmailVerificationStatus(String email, int verifiedStatus);
    
    /**
     * 检查邮箱是否已注册
     */
    boolean existsByEmail(String email);
}