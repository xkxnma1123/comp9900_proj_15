package com.comp9900.proj_15.service;

import com.comp9900.proj_15.entity.Friends;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  Service Class
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
public interface FriendsService extends IService<Friends> {
    /**
     * Send friend request
     */
    void sendFriendRequest(Integer userId, Integer friendId);
    
    /**
     * Process friend request
     */
    void processFriendRequest(Integer userId, Integer friendId, String action);
    
    /**
     * Get friend request list
     */
    List<Map<String, Object>> getFriendRequests(Integer userId);
    
    /**
     * Get friend list
     */
    List<Map<String, Object>> getFriends(Integer userId);
}
