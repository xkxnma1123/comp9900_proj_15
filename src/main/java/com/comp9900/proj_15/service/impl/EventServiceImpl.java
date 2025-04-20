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

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  service implementation class
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
    public List<Event> getActivityList(Integer userId) {
        // 1. Query all events, sorted by date in descending order
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Event::getDate);
        List<Event> eventList = this.list(queryWrapper);

        // 2. Query all events and statuses in which the user participates
        // Assume that the UserEvent model has uid, eid, status fields
        LambdaQueryWrapper<UserEvent> userEventQuery = new LambdaQueryWrapper<>();
        userEventQuery.eq(UserEvent::getUid, userId);
        List<UserEvent> userEvents = userEventService.list(userEventQuery);

        // 3. Create a mapping of event IDs to states
        Map<Integer, String> eventStatusMap = new HashMap<>();
        for (UserEvent userEvent : userEvents) {
            eventStatusMap.put(userEvent.getEid(), userEvent.getStatus());
        }

        // 4. add status field to each event
        for (Event event : eventList) {
            // If the user participates in the event and the status is attend, set the status to attend, otherwise to none
            String status = "none";
            if (eventStatusMap.containsKey(event.getId())) {
                String userEventStatus = eventStatusMap.get(event.getId());
                if ("attend".equals(userEventStatus)) {
                    status = "attend";
                }
            }

            // Set the status field for the event (assuming the Event class has a status field or can be set by other means)
            event.setStatus(status);
        }

        return eventList;
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
    public Map<String, Object> getActivityWithParticipants(Long id, Integer userId) {
        // Get the event by id
        Event event = this.getById(id);

        if (event == null) {
            return null;
        }

        // check if the user is in the event
        QueryWrapper<UserEvent> userEventQuery = new QueryWrapper<>();
        userEventQuery.eq("EID", id)
                .eq("UID", userId);
        UserEvent userEvent = userEventService.getOne(userEventQuery);

        // set the status based on userEvent
        String status = "none";
        if (userEvent != null && "attend".equals(userEvent.getStatus())) {
            status = "attend";
        }
        event.setStatus(status);

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
        for (UserEvent ue : userEvents) {
            Integer uid = ue.getUid();
            User user = userMapper.selectById(uid);

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

//
    public Map<String, Object> checkUserFriendship(Integer eventID, Integer myUserId) {
        // create a map to store the result
        Map<String, Object> result = new HashMap<>();

        // get my friends
        List<Map<String, Object>> myFriends = friendsService.getFriendsStatus(myUserId);

        // find all users who participated in the event
        List<Map<String, Object>> participants = new ArrayList<>();

        // Find all users who participated in this activity
        QueryWrapper<UserEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("EID", eventID)
                .eq("Status", "attend");  // Include only users who have participated
        List<UserEvent> userEvents = userEventService.list(queryWrapper);

        // For each participating relationship, get the user details
        for (UserEvent userEvent : userEvents) {
            Integer participantId = userEvent.getUid();

            // Skip the current user
            if (participantId.equals(myUserId)) {
                continue;
            }

            User participantUser = userMapper.selectById(participantId);

            if (participantUser != null) {
                Map<String, Object> participantInfo = new HashMap<>();
                participantInfo.put("id", participantUser.getId());
                participantInfo.put("name", participantUser.getName());

                // Check if this participant is a friend of the current user and get the friend status
                String friendStatus = "none";  

                // Find matching friend records
                Optional<Map<String, Object>> friendRecord = myFriends.stream()
                        .filter(f -> f.containsKey("Friend_ID") && f.get("Friend_ID").equals(participantId))
                        .findFirst();

                // If a friend record is found, get the Status field
                if (friendRecord.isPresent()) {
                    friendStatus = friendRecord.get().containsKey("Status") ?
                            String.valueOf(friendRecord.get().get("Status")) :
                            "none";
                }

                // Add a new required field and use friendStatus to replace the original isFriend
                participantInfo.put("friendStatus", friendStatus);
                participantInfo.put("userUni", participantUser.getUserUni());
                participantInfo.put("userField", participantUser.getUserField());

                participants.add(participantInfo);
            }
        }

        // Add the list of participants to the results
        result.put("participants", participants);

        return result;
    }




}
