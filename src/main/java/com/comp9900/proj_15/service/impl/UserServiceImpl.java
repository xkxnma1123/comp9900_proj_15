package com.comp9900.proj_15.service.impl;

import com.comp9900.proj_15.mapper.UserMapper;
import com.comp9900.proj_15.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> register(String name, String email, String levelOfStudy, String password) {
        // 检查邮箱是否已存在
        Long count = userMapper.countByEmail(email);
        
        if (count > 0) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        try {
            // 使用MD5加密密码
            String hashedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
            
            // 插入用户数据
            userMapper.insertUser(
                name,
                email,
                levelOfStudy,
                hashedPassword,
                LocalDateTime.now(),
                "normal"
            );
            
            // 查询并返回用户信息
            Map<String, Object> user = userMapper.findUserByEmail(email);
            
            if (user == null || user.isEmpty()) {
                throw new RuntimeException("用户创建失败");
            }
            
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建用户失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> login(String email, String password) {
        // 获取用户密码
        String storedPassword = userMapper.getPasswordByEmail(email);
        
        if (storedPassword == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证密码
        String hashedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!storedPassword.equals(hashedPassword)) {
            throw new RuntimeException("密码错误");
        }
        
        // 查询并返回用户信息
        Map<String, Object> user = userMapper.findUserByEmail(email);
        
        if (user == null || user.isEmpty()) {
            throw new RuntimeException("获取用户信息失败");
        }
        
        return user;
    }
}