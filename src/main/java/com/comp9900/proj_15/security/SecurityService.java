package com.comp9900.proj_15.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("securityService")
public class SecurityService {

    /**
     * Check if current user is the owner of specified resource
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
     * Check if user has specific role
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }

    /**
     * Check if user is resource owner or admin
     */
    public boolean isOwnerOrAdmin(Long resourceUserId) {
        return isResourceOwner(resourceUserId) || hasRole("ADMIN");
    }
}
