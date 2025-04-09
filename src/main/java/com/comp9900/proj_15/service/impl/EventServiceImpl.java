package com.comp9900.proj_15.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comp9900.proj_15.entity.Event;
import com.comp9900.proj_15.entity.User;
import com.comp9900.proj_15.entity.UserEvent;
import com.comp9900.proj_15.mapper.EventMapper;
import com.comp9900.proj_15.mapper.UserMapper;
import com.comp9900.proj_15.service.EventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.comp9900.proj_15.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService {

    @Autowired
    private UserEventServiceImpl userEventService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private FriendsService friendsService;

    @Autowired
    @Lazy
    private EventServiceImpl eventService;

    @Override
    public List<Event> getActivityList() {
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Event::getDate);
        return this.list(queryWrapper);
    }

    @Override
    public IPage<Event> getActivityListPage(Page<Event> page) {
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Event::getDate);
        return this.page(page, queryWrapper);
    }

    @Override
    public Event getActivityById(Long id) {
        return this.getById(id);
    }


    @Override
    public Map<String, Object> getActivityWithParticipants(Long id) {
//        // Get the event by id
//        Event event = this.getById(id);
//
//        if (event == null) {
//            return null;
//        }
//
//        // Create the result map
//        Map<String, Object> result = new HashMap<>();
//        result.put("event", event);
//
//        // Query participants for this event
//        List<Map<String, Object>> participants = new ArrayList<>();
//
//        // Find all user-event relationships for this event with status "attended"
//        QueryWrapper<UserEvent> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("EID", id)
//                .eq("Status", "attend");  // Only include users who attended
//        List<UserEvent> userEvents = userEventService.list(queryWrapper);
//
//        // For each user in the relationship, get their details using UserMapper
//        for (UserEvent userEvent : userEvents) {
//            Integer userId = userEvent.getUid();
//            User user = userMapper.selectById(userId);
//
//            if (user != null) {
//                Map<String, Object> participantInfo = new HashMap<>();
//                participantInfo.put("id", user.getId());
//                participantInfo.put("name", user.getName());
//                participants.add(participantInfo);
//            }
//        }
//
//        // Add participants list to the result
//        result.put("participants", participants);
//
//        return result;
        // Get the event by id
        Event event = this.getById(id);

        if (event == null) {
            return null;
        }

        // Create the result map
        Map<String, Object> result = new HashMap<>();
        result.put("event", event);

        // Query participants for this event
        List<Map<String, Object>> participants = new ArrayList<>();

        // Find all user-event relationships for this event with status "attended"
        QueryWrapper<UserEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("EID", id)
                .eq("Status", "attend");  // Only include users who attended
        List<UserEvent> userEvents = userEventService.list(queryWrapper);

        // For each user in the relationship, get their details using UserMapper
        for (UserEvent userEvent : userEvents) {
            Integer userId = userEvent.getUid();
            User user = userMapper.selectById(userId);

            if (user != null) {
                Map<String, Object> participantInfo = new HashMap<>();
                participantInfo.put("id", user.getId());
                participantInfo.put("name", user.getName());
                participants.add(participantInfo);
            }
        }

        // Add participant count to the result instead of the full list
        result.put("participants", participants.size());

        return result;
    }

    /**
     * Check if a user is in another user's friends list
     *
     * @param eventID The user to check
     * @param myUserId The logged-in user's ID
     * @return Map containing user info and friendship status
     */
    @Override
    public Map<String, Object> checkUserFriendship(Integer eventID, Integer myUserId) {
        // 创建结果映射
        Map<String, Object> result = new HashMap<>();

        // 获取我的好友列表
        List<Map<String, Object>> myFriends = friendsService.getFriends(myUserId);

        // 查询该活动的参与者
        List<Map<String, Object>> participants = new ArrayList<>();

        // 查找所有参与该活动的用户
        QueryWrapper<UserEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("EID", eventID)
                .eq("Status", "attend");  // 只包括已参加的用户
        List<UserEvent> userEvents = userEventService.list(queryWrapper);

        // 对于每个参与关系，获取用户详细信息
        for (UserEvent userEvent : userEvents) {
            Integer participantId = userEvent.getUid();

            // 跳过当前用户自己
            if (participantId.equals(myUserId)) {
                continue;
            }

            User participantUser = userMapper.selectById(participantId);

            if (participantUser != null) {
                Map<String, Object> participantInfo = new HashMap<>();
                participantInfo.put("id", participantUser.getId());
                participantInfo.put("name", participantUser.getName());

                // 检查此参与者是否是当前用户的好友
                boolean isParticipantFriend = myFriends.stream()
                        .anyMatch(f -> f.containsKey("Friend_ID") && f.get("Friend_ID").equals(participantId));

                // 添加新的必需字段
                participantInfo.put("isFriend", isParticipantFriend ? "1" : "0");
                participantInfo.put("userUni", participantUser.getUserUni());
                participantInfo.put("userField", participantUser.getUserField());

                participants.add(participantInfo);
            }
        }

        // 将参与者列表添加到结果中
        result.put("participants", participants);

        return result;
    }




}
