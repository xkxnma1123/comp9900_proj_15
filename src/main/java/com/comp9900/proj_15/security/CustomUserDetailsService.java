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
    private UserMapper userMapper; // Assume you have this Mapper

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Try to parse username as user ID
            Long userId = Long.parseLong(username);

            // Find user by ID
            Map<String, Object> userMap = userMapper.findUserById(userId);

            if (userMap == null || userMap.isEmpty()) {
                throw new UsernameNotFoundException("User not found with id: " + userId);
            }

            // Extract data from Map
            String email = (String) userMap.get("email");
            String password = (String) userMap.get("password");
            String role = userMap.getOrDefault("user_type", "normal").toString();

            // Create and return UserDetails object
            return new User(
                    username, // Use ID as username
                    password,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
            );
        } catch (NumberFormatException e) {
            // If cannot parse as ID, try to find by email (backward compatibility)
            Map<String, Object> userMap = userMapper.findUserByEmail(username);

            if (userMap == null || userMap.isEmpty()) {
                throw new UsernameNotFoundException("User not found with email: " + username);
            }

            // Extract data from Map
            String password = (String) userMap.get("password");
            String role = userMap.getOrDefault("user_type", "normal").toString();

            // Create and return UserDetails object
            return new User(
                    username,
                    password,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
            );
        }
    }
}