package com.comp9900.proj_15.security;

import com.comp9900.proj_15.mapper.UserMapper;
import com.comp9900.proj_15.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper; // 假设你有这个Mapper

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // 尝试将username解析为用户ID
            Long userId = Long.parseLong(username);

            // 通过ID查找用户
            Map<String, Object> userMap = userMapper.findUserById(userId);

            if (userMap == null || userMap.isEmpty()) {
                throw new UsernameNotFoundException("User not found with id: " + userId);
            }

            // 从Map中提取数据
            String email = (String) userMap.get("email");
            String password = (String) userMap.get("password");
            String role = userMap.getOrDefault("user_type", "normal").toString();

            // 创建并返回UserDetails对象
            return new User(
                    username, // 使用ID作为username
                    password,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
            );
        } catch (NumberFormatException e) {
            // 如果无法解析为ID，尝试通过邮箱查找（向后兼容）
            Map<String, Object> userMap = userMapper.findUserByEmail(username);

            if (userMap == null || userMap.isEmpty()) {
                throw new UsernameNotFoundException("User not found with email: " + username);
            }

            // 从Map中提取数据
            String password = (String) userMap.get("password");
            String role = userMap.getOrDefault("user_type", "normal").toString();

            // 创建并返回UserDetails对象
            return new User(
                    username,
                    password,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
            );
        }
    }
}