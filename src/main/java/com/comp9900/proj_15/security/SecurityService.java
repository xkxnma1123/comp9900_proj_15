package com.comp9900.proj_15.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("securityService")
public class SecurityService {

    /**
     * 检查当前用户是否是指定资源的所有者
     */
    public boolean isResourceOwner(Long resourceUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        String currentUserId = authentication.getName();
        return currentUserId != null && currentUserId.equals(resourceUserId.toString());
    }

    /**
     * 检查用户是否有特定角色
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }

    /**
     * 检查用户是否为资源所有者或管理员
     */
    public boolean isOwnerOrAdmin(Long resourceUserId) {
        return isResourceOwner(resourceUserId) || hasRole("ADMIN");
    }
}
