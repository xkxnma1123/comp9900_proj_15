package com.comp9900.proj_15.controller;

import com.comp9900.proj_15.common.R;
import com.comp9900.proj_15.service.UserService;
import com.comp9900.proj_15.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import javax.annotation.security.PermitAll;

/**
 * <p>
 *  验证码控制器
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
@RestController
@RequestMapping("/verification")
public class VerificationController {

    @Autowired
    private VerificationService verificationService;
    
    @Autowired
    private UserService userService;

    /**
     * 验证邮箱
     * @param params 包含邮箱和验证码的参数
     * @return 验证结果
     */
    @PostMapping("/verify")
    @PermitAll
    public R<String> verifyEmail(@RequestBody Map<String, String> params) {
        System.out.println("Verify endpoint called with params: " + params);
        
        String email = params.get("email");
        String code = params.get("code");
        
        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            return R.error("邮箱不能为空");
        }
        if (code == null || code.trim().isEmpty()) {
            return R.error("验证码不能为空");
        }
        
        try {
            // 检查邮箱是否存在
            if (!userService.existsByEmail(email)) {
                return R.error("该邮箱未注册");
            }
            
            // 验证验证码
            boolean isValid = verificationService.verifyCode(email, code);
            
            if (!isValid) {
                return R.error("验证码无效或已过期");
            }
            
            // 更新用户邮箱验证状态
            //userService.updateEmailVerificationStatus(email, 1);
            
            return R.success("邮箱验证成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("验证失败: " + e.getMessage());
        }
    }
    
    /**
     * 重发验证码
     * @param params 包含邮箱的参数
     * @return 重发结果
     */
    @PostMapping("/resend")
    @PermitAll
    public R<String> resendVerificationCode(@RequestBody Map<String, String> params) {
        System.out.println("Resend verification code endpoint called with params: " + params);
        
        String email = params.get("email");
        
        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            return R.error("邮箱不能为空");
        }
        
        try {
            // 检查邮箱是否存在
            if (!userService.existsByEmail(email)) {
                return R.error("该邮箱未注册");
            }
            
            // 重发验证码
            verificationService.resendVerificationCode(email);
            
            return R.success("验证码已重新发送到您的邮箱");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("重发验证码失败: " + e.getMessage());
        }
    }
    @GetMapping("/test")
    public R<String> test() {
        return R.success("VerificationController工作正常");
}
}