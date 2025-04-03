package com.comp9900.proj_15.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.comp9900.proj_15.entity.VerificationCode;
import com.comp9900.proj_15.mapper.VerificationCodeMapper;
import com.comp9900.proj_15.service.EmailService;
import com.comp9900.proj_15.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationServiceImpl extends ServiceImpl<VerificationCodeMapper, VerificationCode> implements VerificationService {
    
    @Autowired
    private VerificationCodeMapper verificationCodeMapper;
    
    @Autowired
    private EmailService emailService;
    
    @Override
    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 生成6位数验证码
        return String.valueOf(code);
    }
    
    @Override
    public void sendVerificationCode(String email) {
        String code = generateVerificationCode();
        saveVerificationCode(email, code);
        emailService.sendVerificationEmail(email, code);
    }
    
    @Override
    public void resendVerificationCode(String email) {
        // 将之前的验证码标记为已使用
        verificationCodeMapper.invalidateAllCodesForEmail(email);
        
        // 生成并发送新的验证码
        String code = generateVerificationCode();
        saveVerificationCode(email, code);
        emailService.sendVerificationEmail(email, code);
    }
    
    private void saveVerificationCode(String email, String code) {
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10);
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpiryDate(expiryDate);
        verificationCode.setUsed(0);
    
        // 使用 MP 提供的 insert 方法
        this.save(verificationCode);
    }
    
    
    @Override
    public boolean verifyCode(String email, String code) {
        // 获取最新的未使用验证码
        VerificationCode verificationCode = verificationCodeMapper.findLatestByEmail(email);
        
        if (verificationCode == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(verificationCode.getExpiryDate())) {
            verificationCodeMapper.markAsUsed(verificationCode.getId()); // 标记过期验证码为已使用
            return false;
        }
        
        boolean isValid = verificationCode.getCode().equals(code);
        
        if (isValid) {
            // 标记验证码为已使用
            verificationCodeMapper.markAsUsed(verificationCode.getId());
        }
        
        return isValid;
    }
    
    // 每天凌晨2点清理过期和已使用的验证码
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredCodes() {
        verificationCodeMapper.deleteUsedAndExpiredCodes();
    }
}