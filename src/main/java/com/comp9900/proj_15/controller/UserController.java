package com.comp9900.proj_15.controller;


import com.comp9900.proj_15.common.R;
import com.comp9900.proj_15.service.UserService;
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

import java.util.List;
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


    @Autowired
    private JwtUtil jwtUtil;  // 注入JwtUtil

    /**
<<<<<<< Updated upstream
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
        
//        try {
//            // 注册用户
//            Map<String, Object> userMap = userService.register(
//                params.get("name"),
//                params.get("email"),
//                params.get("level_of_study"),
//                params.get("password")
//            );
        try {

            User user = new User();
            // 设置所有字段，直接从参数获取
            user.setName(params.get("name"));
            user.setEmail(params.get("email"));
            user.setLevelOfStudy(params.get("level_of_study"));
            user.setPasswordHash(params.get("password")); // 在service层会进行MD5加密
            //user.setUserType(params.get("user_type"));
            user.setUserCity(params.get("userCity"));
            user.setUserCountry(params.get("userCountry"));
            user.setUserField(params.get("userField"));
            user.setUserLanguage(params.get("userLanguage"));
            user.setUserRegions(params.get("userRegions"));
            user.setUserUni(params.get("userUni"));


            // 注册用户
            Map<String, Object> userMap = userService.register(user);

//            // 生成JWT Token - 确保不为null
//            if (userMap != null) {
//                String token = jwtUtil.generateToken(userMap);
//                // 将token添加到返回结果中
//                userMap.put("token", token);
//            }
            return R.success("注册成功", userMap);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

//    /**
//     * 更新用户信息（包含地址信息）
//     */
//    @PutMapping("/register/{id}")
//    public R<User> profileAfterRegister(
//            @PathVariable Integer id,
//            @RequestBody User user) {
//        user.setId(id); // 确保ID正确设置
//        User updatedUser = userService.updateUser(user);
//        return R.success("用户信息更新成功", updatedUser);
//    }

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

            // 生成JWT Token - 确保不为null
            if (userMap != null) {
                String token = jwtUtil.generateToken(userMap);
                // 将token添加到返回结果中
                userMap.put("token", token);
            }

            return R.success("登录成功", userMap);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }
//    public R<Map<String, Object>> login(@RequestBody Map<String, String> params) {
//        System.out.println("Login endpoint called with params: " + params);
//
//        String email = params.get("email");
//        String password = params.get("password");
//
//        // 参数校验
//        if (email == null || email.trim().isEmpty()) {
//            return R.error("邮箱不能为空");
//        }
//        if (password == null || password.trim().isEmpty()) {
//            return R.error("密码不能为空");
//        }
//
//        try {
//            // 登录
//            Map<String, Object> userMap = userService.login(email, password);
//            return R.success("登录成功", userMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return R.error(e.getMessage());
//        }
//    }
    
    /**
     * 测试接口
     */
    @GetMapping("/test")
    public String test() {
        return "UserController 工作正常";
    }

//     * 获取所有国家列表
//     */
    @GetMapping("/locations/countries")
    public R<List<Map<String, Object>>> getAllCountries() {
        List<Map<String, Object>> countries = userService.getAllCountries();
        return R.success(countries);
    }

    /**
     * 根据国家代码获取省份/地区列表
     */
    @GetMapping("/locations/regions")
    public R<List<Map<String, Object>>> getRegionsByCountry(@RequestParam String countryCode) {
        List<Map<String, Object>> regions = userService.getRegionsByCountry(countryCode);
        return R.success(regions);
    }

    /**
     * 根据国家代码和地区代码获取城市列表
     */
    @GetMapping("/locations/cities")
    public R<List<Map<String, Object>>> getCitiesByRegion(
            @RequestParam String countryCode,
            @RequestParam String regionCode) {
        List<Map<String, Object>> cities = userService.getCitiesByRegion(countryCode, regionCode);
        return R.success(cities);
    }

//    /**
//     * 注册新用户（包含地址信息）
//     */
//    @PostMapping("/register")
//    public R<User> registerUser(@RequestBody User user) {
//        User registeredUser = userService.registerUser(user);
//        return R.success("用户注册成功", registeredUser);
//    }

    /**
     * 更新用户信息（包含地址信息）
     */
    @PutMapping("/{id}")
    @PreAuthorize("authentication.principal.username == #id.toString() or hasRole('ADMIN')")
    public R<User> updateUser(
            @PathVariable Integer id,
            @RequestBody User user) {
        user.setId(id); // 确保ID正确设置
        User updatedUser = userService.updateUser(user);
        return R.success("用户信息更新成功", updatedUser);
    }


    /**
     * 根据条件随机分页查询用户
     *
     * @param current 当前页
     * @param size 每页大小
     * @param field 专业领域
     * @param university 大学
     * @param city 城市
     * @return 分页结果
     */
    @GetMapping("/randomPage")
    @ApiOperation("根据条件随机分页查询用户")
    public R<Page<User>> randomPageByConditions(
            @ApiParam(value = "当前页", required = true) @RequestParam(defaultValue = "1") Long current,
            @ApiParam(value = "每页大小", required = true) @RequestParam(defaultValue = "10") Long size,
            @ApiParam(value = "专业领域") @RequestParam(required = false) String field,
            @ApiParam(value = "大学") @RequestParam(required = false) String university,
            @ApiParam(value = "城市") @RequestParam(required = false) String city) {

        // 创建分页对象
        Page<User> page = new Page<>(current, size);

        // 调用Service进行随机分页查询
        Page<User> resultPage = userService.randomPageByConditions(page, field, university, city);

        return R.success(resultPage);
    }

    /**
     * 获取用户的城市、大学和专业字段信息
     * @param userId 用户ID
     * @return 包含用户城市、大学和专业字段的响应
     */
    @GetMapping("/peerMatchInfo/{userId}")
    @PreAuthorize("authentication.principal.username == #id.toString() or hasRole('ADMIN')")
    public R<Map<String, String>> getUserInfo(@PathVariable Integer userId) {
        try {
            Map<String, String> userInfo = userService.getUserPeerMatchInfo(userId);
            return R.success("获取用户信息成功", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取用户信息失败: " + e.getMessage());
        }
    }

}

