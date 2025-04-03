package com.comp9900.proj_15.service;

/**
 * 邮件服务接口
 */
public interface EmailService {
    /**
     * 发送验证码邮件
     */
    void sendVerificationEmail(String to, String code);
}