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
 *  friends service implementation class
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
        // check if user exists
        Long userExists = userMapper.countById(userId);
        Long friendExists = userMapper.countById(friendId);
        
        if (userExists == 0) {
            throw new RuntimeException("sender user does not exist");
        }
        
        if (friendExists == 0) {
            throw new RuntimeException("receiver user does not exist");
        }
        
        // check if friend request already exists or if they are already friends
        Long friendRequestExists = friendsMapper.checkFriendRequestExists(userId, friendId);
        
        if (friendRequestExists > 0) {
            throw new RuntimeException("already sent a friend request or already friends");
        }
        
        // send friend request
        friendsMapper.addFriendRequest(userId, friendId);
    }
    
    @Override
    @Transactional
    public void processFriendRequest(Integer userId, Integer friendId, String action) {
        // get the friend request
        Friends request = friendsMapper.getFriendRequest(userId, friendId);
        
        if (request == null) {
            throw new RuntimeException("the friend request does not exist");
        }
        
        if (!request.getStatus().equals("pending")) {
            throw new RuntimeException("this friend request has already been processed");
        }
        
        if ("accept".equals(action)) {
            // accept friend request - ensure using the correct status value
            friendsMapper.updateFriendRequestStatus(userId, friendId, "accept");
            
            // create a reverse friend relationship
            friendsMapper.addReverseFriend(userId, friendId);
        } else if ("reject".equals(action)) {
            // reject friend request
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
