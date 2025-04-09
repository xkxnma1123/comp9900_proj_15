package com.comp9900.proj_15.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.comp9900.proj_15.entity.VerificationCode;

public interface VerificationService extends IService<VerificationCode> {
    String generateVerificationCode();
    void sendVerificationCode(String email);
    void resendVerificationCode(String email);
    boolean verifyCode(String email, String code);
}