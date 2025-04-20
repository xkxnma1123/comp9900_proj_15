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
 *  verification code controller
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
     * verify email and code
     * @param params contains email and code
     * @return verification result
     */
    @PostMapping("/verify")
    @PermitAll
    public R<String> verifyEmail(@RequestBody Map<String, String> params) {
        System.out.println("Verify endpoint called with params: " + params);
        
        String email = params.get("email");
        String code = params.get("code");
        
        if (email == null || email.trim().isEmpty()) {
            return R.error("email cannot be empty");
        }
        if (code == null || code.trim().isEmpty()) {
            return R.error("verification code cannot be empty");
        }
        
        try {
            // check if email exists
            if (!userService.existsByEmail(email)) {
                return R.error("email not registered");
            }
            
            // verify the code
            boolean isValid = verificationService.verifyCode(email, code);
            
            if (!isValid) {
                return R.error("code is invalid or expired");
            }
            

            
            return R.success("verification successful");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("verfication false: " + e.getMessage());
        }
    }
    
    /**
     * resend verification code
     * @param params contains email
     * @return resend result
     */
    @PostMapping("/resend")
    @PermitAll
    public R<String> resendVerificationCode(@RequestBody Map<String, String> params) {
        System.out.println("Resend verification code endpoint called with params: " + params);
        
        String email = params.get("email");
        

        if (email == null || email.trim().isEmpty()) {
            return R.error("email cannot be empty");
        }
        
        try {
            // check if email exists
            if (!userService.existsByEmail(email)) {
                return R.error("this email is not registered");
            }
            
            // resend the verification code
            verificationService.resendVerificationCode(email);
            
            return R.success("verification code resent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("resend false: " + e.getMessage());
        }
    }
    @GetMapping("/test")
    public R<String> test() {
        return R.success("VerificationController is working");
}
}