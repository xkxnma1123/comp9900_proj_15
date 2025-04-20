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
 *  user event service implementation
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
     * user attends an event
     *
     * @param userId 
     * @param eventId 
     * @return created UserEvent record
     */
    @Override
    @Transactional
    public UserEvent attendEvent(Long userId, Long eventId) {
        // check if user exists
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("this user does not exist: " + userId);
        }

//         check if event exists
        Event event = eventService.getById(eventId);
        if (event == null) {
            throw new RuntimeException("event does not exist: " + eventId);
        }

        // Check if the user has already participated in this event
        LambdaQueryWrapper<UserEvent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEvent::getUid, userId)
                .eq(UserEvent::getEid, eventId)
                .eq(UserEvent::getStatus, "attend");

        UserEvent existingUserEvent = getOne(queryWrapper, false);

        if (existingUserEvent != null) {
            throw new RuntimeException("user has already attended this event");
        }

        // Find the activity records that the user attended
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEvent::getUid, userId)
                .eq(UserEvent::getEid, eventId);

        existingUserEvent = getOne(queryWrapper, false);

        // Create or update a UserEvent record
        UserEvent userEvent;
        boolean shouldAwardCoins = false;

        if (existingUserEvent != null) {
            userEvent = existingUserEvent;
            // Tokens are awarded only when checkFlag is false
            shouldAwardCoins = !userEvent.isCheckFlag();
        } else {
            userEvent = new UserEvent();
            userEvent.setUid(userId.intValue());
            userEvent.setEid(eventId.intValue());
            userEvent.setCheckFlag(true); // Set checkFlag to true for new records
            shouldAwardCoins = true; // New records should receive tokens
        }

        // set the status to "attend"
        userEvent.setStatus("attend");

        // save or update the record
        saveOrUpdate(userEvent);

        // Determine whether to award tokens to users based on shouldAwardCoins
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
     * user quits an event
     * only change the status to "quit", do not change the check flag
     *
     * @param userId 
     * @param eventId 
     * @return updated UserEvent object
     */
    @Override
    @Transactional
    public UserEvent quitEvent(Long userId, Long eventId) {
        // check if user exists
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("user does not exist, id: " + userId);
        }

        // check if event exists
        Event event = eventService.getById(eventId);
        if (event == null) {
            throw new RuntimeException("event does not exist, id: " + eventId);
        }

        // Find the activity records that the user attended
        LambdaQueryWrapper<UserEvent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEvent::getUid, userId)
                .eq(UserEvent::getEid, eventId);

        UserEvent userEvent = getOne(queryWrapper, false);

        if (userEvent == null) {
            throw new RuntimeException("user has not attended this event");
        }

        // update the status to "quit", do not change the check flag
        userEvent.setStatus("quit");

        // update the record
        updateById(userEvent);

        return userEvent;
    }

}
