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
        int code = 100000 + random.nextInt(900000); // generate a 6-digit code
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
        // mark all previous unused codes as used
        verificationCodeMapper.invalidateAllCodesForEmail(email);
        
        // generate a new code and send it
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
    
        // insert the new verification code into the database
        this.save(verificationCode);
    }
    
    
    @Override
    public boolean verifyCode(String email, String code) {
        // get the latest unused verification code for the email
        VerificationCode verificationCode = verificationCodeMapper.findLatestByEmail(email);
        
        if (verificationCode == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(verificationCode.getExpiryDate())) {
            verificationCodeMapper.markAsUsed(verificationCode.getId()); // mark as used if expired
            return false;
        }
        
        boolean isValid = verificationCode.getCode().equals(code);
        
        if (isValid) {
            // mark the code as used
            verificationCodeMapper.markAsUsed(verificationCode.getId());
        }
        
        return isValid;
    }
    
    // cleanup task to delete used and expired verification codes
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredCodes() {
        verificationCodeMapper.deleteUsedAndExpiredCodes();
    }
}