package com.comp9900.proj_15.service.impl;

import com.comp9900.proj_15.entity.Friends;
import com.comp9900.proj_15.mapper.FriendsMapper;
import com.comp9900.proj_15.mapper.UserMapper;
import com.comp9900.proj_15.service.FriendsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
@Service
public class FriendsServiceImpl extends ServiceImpl<FriendsMapper, Friends> implements FriendsService {
    @Autowired
    private FriendsMapper friendsMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public void sendFriendRequest(Integer userId, Integer friendId) {
        // 检查用户是否存在
        Long userExists = userMapper.countById(userId);
        Long friendExists = userMapper.countById(friendId);
        
        if (userExists == 0) {
            throw new RuntimeException("发送者用户不存在");
        }
        
        if (friendExists == 0) {
            throw new RuntimeException("接收者用户不存在");
        }
        
        // 检查是否已经是好友或已发送请求
        Long friendRequestExists = friendsMapper.checkFriendRequestExists(userId, friendId);
        
        if (friendRequestExists > 0) {
            throw new RuntimeException("已经发送过好友请求或已经是好友");
        }
        
        // 发送好友请求
        friendsMapper.addFriendRequest(userId, friendId);
    }
    
    @Override
    @Transactional
    public void processFriendRequest(Integer userId, Integer friendId, String action) {
        // 获取好友请求
        Friends request = friendsMapper.getFriendRequest(userId, friendId);
        
        if (request == null) {
            throw new RuntimeException("好友请求不存在");
        }
        
        if (!request.getStatus().equals("pending")) {
            throw new RuntimeException("该请求已处理");
        }
        
        if ("accept".equals(action)) {
            // 接受好友请求 - 确保使用正确的状态值
            friendsMapper.updateFriendRequestStatus(userId, friendId, "accept");
            
            // 创建反向好友关系
            friendsMapper.addReverseFriend(userId, friendId);
        } else if ("reject".equals(action)) {
            // 拒绝好友请求 - 确保使用正确的状态值
            friendsMapper.updateFriendRequestStatus(userId, friendId, "reject");
        }
    }
    
    @Override
    public List<Map<String, Object>> getFriendRequests(Integer userId) {
        return friendsMapper.getFriendRequests(userId);
    }
    
    @Override
    public List<Map<String, Object>> getFriends(Integer userId) {
        return friendsMapper.getFriends(userId);
    }


    @Override
    public List<Map<String, Object>> getFriendsStatus(Integer userId) {
        return friendsMapper.getFriendsStatus(userId);
    }
}
