package com.comp9900.proj_15.controller;

import com.comp9900.proj_15.common.R;
import com.comp9900.proj_15.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param params 包含用户信息的参数
     * @return 注册结果
     */
    @PostMapping("/register")
    public R<Map<String, Object>> register(@RequestBody Map<String, String> params) {
        System.out.println("Register endpoint called with params: " + params);
        
        // 参数校验
        if (params.get("name") == null || params.get("name").trim().isEmpty()) {
            return R.error("用户名不能为空");
        }
        if (params.get("email") == null || params.get("email").trim().isEmpty()) {
            return R.error("邮箱不能为空");
        }
        if (params.get("password") == null || params.get("password").trim().isEmpty()) {
            return R.error("密码不能为空");
        }
        if (params.get("level_of_study") == null || params.get("level_of_study").trim().isEmpty()) {
            return R.error("学习层次不能为空");
        }
        
        try {
            // 注册用户
            Map<String, Object> userMap = userService.register(
                params.get("name"), 
                params.get("email"), 
                params.get("level_of_study"), 
                params.get("password")
            );
            return R.success("注册成功", userMap);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 用户登录
     * @param params 包含登录信息的参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        System.out.println("Login endpoint called with params: " + params);
        
        String email = params.get("email");
        String password = params.get("password");
        
        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            return R.error("邮箱不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return R.error("密码不能为空");
        }
        
        try {
            // 登录
            Map<String, Object> userMap = userService.login(email, password);
            return R.success("登录成功", userMap);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }
    
    /**
     * 测试接口
     */
    @GetMapping("/test")
    public String test() {
        return "UserController 工作正常";
    }
}