package com.comp9900.proj_15.service;

import com.comp9900.proj_15.entity.Friends;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
public interface FriendsService extends IService<Friends> {
    /**
     * 发送好友请求
     */
    void sendFriendRequest(Integer userId, Integer friendId);
    
    /**
     * 处理好友请求
     */
    void processFriendRequest(Integer userId, Integer friendId, String action);
    
    /**
     * 获取好友请求列表
     */
    List<Map<String, Object>> getFriendRequests(Integer userId);
    
    /**
     * 获取好友列表
     */
    List<Map<String, Object>> getFriends(Integer userId);
}
