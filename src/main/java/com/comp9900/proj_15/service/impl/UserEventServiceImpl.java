package com.comp9900.proj_15.service.impl;

import com.comp9900.proj_15.entity.UserEvent;
import com.comp9900.proj_15.mapper.UserEventMapper;
import com.comp9900.proj_15.service.UserEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.comp9900.proj_15.entity.Event;
import com.comp9900.proj_15.entity.User;
import com.comp9900.proj_15.entity.UserEvent;
import com.comp9900.proj_15.mapper.UserEventMapper;
import com.comp9900.proj_15.service.EventService;
import com.comp9900.proj_15.service.UserEventService;
import com.comp9900.proj_15.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class UserEventServiceImpl extends ServiceImpl<UserEventMapper, UserEvent> implements UserEventService {
    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private EventService eventService;

    /**
     * 用户参加活动
     *
     * @param userId 用户ID
     * @param eventId 活动ID
     * @return 创建的UserEvent记录
     */
    @Override
    @Transactional
    public UserEvent attendEvent(Long userId, Long eventId) {
        // 检查用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在，ID: " + userId);
        }

//         检查活动是否存在
        Event event = eventService.getById(eventId);
        if (event == null) {
            throw new RuntimeException("活动不存在，ID: " + eventId);
        }

        // 检查用户是否已经参加过此活动
        LambdaQueryWrapper<UserEvent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEvent::getUid, userId)
                .eq(UserEvent::getEid, eventId)
                .eq(UserEvent::getStatus, "attend");

        UserEvent existingUserEvent = getOne(queryWrapper, false);

        if (existingUserEvent != null) {
            throw new RuntimeException("用户已经参加过此活动");
        }

        // 查找是否有其他状态的记录
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEvent::getUid, userId)
                .eq(UserEvent::getEid, eventId);

        existingUserEvent = getOne(queryWrapper, false);

        // 创建或更新UserEvent记录
        UserEvent userEvent;
        boolean shouldAwardCoins = false;

        if (existingUserEvent != null) {
            userEvent = existingUserEvent;
            // 只有当checkFlag为false时才奖励代币
            shouldAwardCoins = !userEvent.isCheckFlag();
        } else {
            userEvent = new UserEvent();
            userEvent.setUid(userId.intValue());
            userEvent.setEid(eventId.intValue());
            userEvent.setCheckFlag(true); // 新记录设置checkFlag为true
            shouldAwardCoins = true; // 新记录应该获得代币
        }

        // 设置状态为attend
        userEvent.setStatus("attend");

        // 保存用户活动记录
        saveOrUpdate(userEvent);

        // 根据shouldAwardCoins判断是否给用户奖励代币
        if (shouldAwardCoins) {
            Integer eventCoin = event.getCoin();
            if (eventCoin != null && eventCoin > 0) {
                Integer userCoin = user.getCoin();
                if (userCoin == null) {
                    userCoin = 0;
                }
                user.setCoin(userCoin + eventCoin);
                userService.updateById(user);
            }
        }

        return userEvent;
    }

    /**
     * 用户退出活动
     * 仅更改状态为quit，不改变check标志
     *
     * @param userId 用户ID
     * @param eventId 活动ID
     * @return 更新后的UserEvent对象
     */
    @Override
    @Transactional
    public UserEvent quitEvent(Long userId, Long eventId) {
        // 检查用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在，ID: " + userId);
        }

        // 检查活动是否存在
        Event event = eventService.getById(eventId);
        if (event == null) {
            throw new RuntimeException("活动不存在，ID: " + eventId);
        }

        // 查找用户参加的活动记录
        LambdaQueryWrapper<UserEvent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEvent::getUid, userId)
                .eq(UserEvent::getEid, eventId);

        UserEvent userEvent = getOne(queryWrapper, false);

        if (userEvent == null) {
            throw new RuntimeException("用户未参加此活动");
        }

        // 更新状态为quit，保持check标志不变
        userEvent.setStatus("quit");

        // 保存更新后的记录
        updateById(userEvent);

        return userEvent;
    }

}
