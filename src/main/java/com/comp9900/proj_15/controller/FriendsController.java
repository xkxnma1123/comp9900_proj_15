package com.comp9900.proj_15.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.comp9900.proj_15.common.R;
import com.comp9900.proj_15.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Map;

/**
 * <p>
 *  front end controller
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
@RestController
@RequestMapping("/friends")
public class FriendsController {
     @Autowired
    private FriendsService friendsService;

    @PostMapping("/request")
    public R<Void> sendFriendRequest(@RequestBody Map<String, Object> params) {
        Integer userId = Integer.parseInt(params.get("userId").toString());
        Integer friendId = Integer.parseInt(params.get("friendId").toString());
        
        try {
            friendsService.sendFriendRequest(userId, friendId);
            
            return R.success((Void) null);  
 
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    
    @PostMapping("/process")
    public R<Void> processFriendRequest(@RequestBody Map<String, Object> params) {
        Integer userId = Integer.parseInt(params.get("userId").toString());
        Integer friendId = Integer.parseInt(params.get("friendId").toString());
        String action = params.get("action").toString();
        
        try {
            friendsService.processFriendRequest(userId, friendId, action);
            String message = action.equals("accept") ? "accept" : "decline";

            return R.success(message, null);

        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    
    /**
     * get friend request list
     * @param userId
     * @return List of friend requests
     */
    @GetMapping("/requests/{userId}")
    public R<List<Map<String, Object>>> getFriendRequests(@PathVariable Integer userId) {
        try {
            List<Map<String, Object>> requests = friendsService.getFriendRequests(userId);
            return R.success(requests);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    
    /**
     * get friend list
     * @param userId
     */
    @GetMapping("/list/{userId}")
    public R<List<Map<String, Object>>> getFriends(@PathVariable Integer userId) {
        try {
            List<Map<String, Object>> friends = friendsService.getFriends(userId);
            return R.success(friends);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

}
