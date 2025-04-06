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
 *  前端控制器
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
            // 选择以下两种调用方式之一:
            // 方式1: 使用数据作为参数 - 消息将是默认的"操作成功"
            return R.success((Void) null);  // 无数据，传null
            
            // 方式2: 使用自定义消息和数据
            // return R.success("好友请求发送成功", null);
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
            // 选择消息内容
            String message = action.equals("accept") ? "已接受好友请求" : "已拒绝好友请求";
            
            // 选择以下两种调用方式之一:
            // 方式1: 使用消息和数据
            return R.success(message, null);
            
            // 方式2: 只使用数据，消息将是默认的"操作成功"
            // return R.success(null);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    
    /**
     * 获取好友请求列表
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
     * 获取好友列表
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
