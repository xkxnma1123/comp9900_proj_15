package com.comp9900.proj_15.controller;


import com.comp9900.proj_15.common.R;
import com.comp9900.proj_15.service.UserService;
import com.comp9900.proj_15.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.comp9900.proj_15.security.JwtUtil;
import com.comp9900.proj_15.entity.User;
import com.comp9900.proj_15.service.UserService;
import com.comp9900.proj_15.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Map;
/**
 * <p>
 *  Frontend Controller
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

    @Autowired
    private VerificationService verificationService;


    @Autowired
    private JwtUtil jwtUtil;  // Inject JwtUtil

    /**
     * User registration
     * @param params Parameters containing user information
     * @return Registration result
     */
    @PostMapping("/register")
    public R<Map<String, Object>> register(@RequestBody Map<String, String> params) {
        System.out.println("Register endpoint called with params: " + params);
        
        // Parameter validation
//        if (params.get("name") == null || params.get("name").trim().isEmpty()) {
//            return R.error("Username cannot be empty");
//        }
//        if (params.get("email") == null || params.get("email").trim().isEmpty()) {
//            return R.error("Email cannot be empty");
//        }
//        if (params.get("password") == null || params.get("password").trim().isEmpty()) {
//            return R.error("Password cannot be empty");
//        }
//        if (params.get("level_of_study") == null || params.get("level_of_study").trim().isEmpty()) {
//            return R.error("Level of study cannot be empty");
//        }
        
        try {
            User user = new User();
            // Set all fields directly from parameters
            user.setName(params.get("name"));
            user.setEmail(params.get("email"));
            user.setLevelOfStudy(params.get("level_of_study"));
            user.setPasswordHash(params.get("password")); // MD5 encryption will be performed in service layer
            user.setUserCity(params.get("userCity"));
            user.setUserCountry(params.get("userCountry"));
            user.setUserField(params.get("userField"));
            user.setUserLanguage(params.get("userLanguage"));
            user.setUserRegions(params.get("userRegions"));
            user.setUserUni(params.get("userUni"));

            // Register user
            Map<String, Object> userMap = userService.register(user);
            return R.success("Registration successful", userMap);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 发验证码
     * @param params 包含邮箱的参数
     * @return 发送结果
     */
    @PostMapping("/register/sendVerifyCode")
    @PermitAll
    public R<String> sendVerificationCode(@RequestBody Map<String, String> params) {
        System.out.println("Resend verification code endpoint called with params: " + params);

        String email = params.get("email");

        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            return R.error("邮箱不能为空");
        }

        try {
            // 检查邮箱是否存在
            if (userService.existsByEmail(email)) {
                return R.error("该邮箱已注册");
            }

            // 重发验证码
            verificationService.sendVerificationCode(email);

            return R.success("验证码已经发送到您的邮箱");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("发送验证码失败: " + e.getMessage());
        }
    }

    @PostMapping("/register/verifyCode")
    @PermitAll
    public R<String> verifyCode(@RequestBody Map<String, String> params) {
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
            if (userService.existsByEmail(email)) {
                return R.error("该邮箱已被注册");
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
     * User login
     * @param params Parameters containing login information
     * @return Login result
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        System.out.println("Login endpoint called with params: " + params);

        String email = params.get("email");
        String password = params.get("password");

        // Parameter validation
        if (email == null || email.trim().isEmpty()) {
            return R.error("Email cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            return R.error("Password cannot be empty");
        }

        try {
            // Login
            Map<String, Object> userMap = userService.login(email, password);

            // Generate JWT Token - ensure not null
            if (userMap != null) {
                String token = jwtUtil.generateToken(userMap);
                // Add token to return result
                userMap.put("token", token);
            }

            return R.success("Login successful", userMap);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }
    
    /**
     * Test endpoint
     */
    @GetMapping("/test")
    public String test() {
        return "UserController is working properly";
    }

    @GetMapping("/locations/countries")
    public R<List<Map<String, Object>>> getAllCountries() {
        List<Map<String, Object>> countries = userService.getAllCountries();
        return R.success(countries);
    }

    /**
     * Get provinces/regions list by country code
     */
    @GetMapping("/locations/regions")
    public R<List<Map<String, Object>>> getRegionsByCountry(@RequestParam String countryCode) {
        List<Map<String, Object>> regions = userService.getRegionsByCountry(countryCode);
        return R.success(regions);
    }

    /**
     * Get cities list by country code and region code
     */
    @GetMapping("/locations/cities")
    public R<List<Map<String, Object>>> getCitiesByRegion(
            @RequestParam String countryCode,
            @RequestParam String regionCode) {
        List<Map<String, Object>> cities = userService.getCitiesByRegion(countryCode, regionCode);
        return R.success(cities);
    }

    /**
     * Update user information (including address information)
     */
    @PutMapping("/{id}")
    @PreAuthorize("authentication.principal.username == #id.toString() or hasRole('ADMIN')")
    public R<User> updateUser(
            @PathVariable Integer id,
            @RequestBody User user) {
        user.setId(id); // Ensure ID is correctly set
        User updatedUser = userService.updateUser(user);
        return R.success("User information updated successfully", updatedUser);
    }

    /**
     * Random pagination query of users based on conditions
     *
     * @param current Current page
     * @param size Page size
     * @param field Professional field
     * @param university University
     * @param city City
     * @return Pagination result
     */
    @GetMapping("/randomPage")
    @ApiOperation("Random pagination query of users based on conditions")
    public R<Page<User>> randomPageByConditions(
            @ApiParam(value = "Current page", required = true) @RequestParam(defaultValue = "1") Long current,
            @ApiParam(value = "Page size", required = true) @RequestParam(defaultValue = "10") Long size,
            @ApiParam(value = "Professional field") @RequestParam(required = false) String field,
            @ApiParam(value = "University") @RequestParam(required = false) String university,
            @ApiParam(value = "City") @RequestParam(required = false) String city,
            @ApiParam(value = "userId") @RequestParam(required = false) Integer userId){

        // Create pagination object
        Page<User> page = new Page<>(current, size);

        // Call Service for random pagination query
        Page<User> resultPage = userService.randomPageByConditions(page, field, university, city, userId);

        return R.success(resultPage);
    }

    /**
     * Get user's city, university and professional field information
     * @param userId User ID
     * @return Response containing user's city, university and professional field
     */
    @GetMapping("/peerMatchInfo/{userId}")
    @PreAuthorize("authentication.principal.username == #id.toString() or hasRole('ADMIN')")
    public R<Map<String, String>> getUserInfo(@PathVariable Integer userId) {
        try {
            Map<String, String> userInfo = userService.getUserPeerMatchInfo(userId);
            return R.success("User information retrieved successfully", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("Failed to get user information: " + e.getMessage());
        }
    }

    /**
     * Get a user's complete information by their ID
     *
     * @param id The ID of the user to retrieve
     * @return R containing the user information
     */
    @GetMapping("/{id}")
    public R<User> getCurrentUserInfo(@PathVariable Integer id) {
        // Get user by ID
        User user = userService.getUserById(id);

        // Check if user exists
        if (user == null) {
            return R.error("User not found");
        }

        // Return user information
        return R.success(user);
    }

}

