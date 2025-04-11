package com.comp9900.proj_15.controller;

import com.comp9900.proj_15.entity.Event;
import com.comp9900.proj_15.service.EventService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comp9900.proj_15.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Frontend Controller
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventService eventService;



    /**
     * @param userId 用户ID
     * @return Event list with status
     */
    @GetMapping("/list")
    public R<List<Event>> getActivityList(@RequestParam Integer userId) {
        List<Event> list = eventService.getActivityList(userId);
        return R.success(list);
    }

    /**
     * @param current Current page
     * @param size Page size
     * @return Paginated event list
     */
    @GetMapping("/page")
    public R<IPage<Event>> getActivityListPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        Page<Event> page = new Page<>(current, size);
        IPage<Event> result = eventService.getActivityListPage(page);
        return R.success(result);
    }

    /**
     * @param id Event ID
     * @return Event details
     */
//    @GetMapping("/detail/{id}")
//    public R<Event> getActivityDetail(@PathVariable Long id) {
//        Event activity = eventService.getActivityById(id);
//        if (activity == null) {
//            return R.error("Event does not exist");
//        }
//        return R.success(activity);
//    }
    @GetMapping("/detail/{id}")
    public R<Map<String, Object>> getActivityDetail(@PathVariable Long id, @RequestParam Integer userId) {
        Map<String, Object> result = eventService.getActivityWithParticipants(id, userId);
        if (result == null) {
            return R.error("Event does not exist");
        }
        return R.success(result);
    }


    /**
     * Check if a user is in the logged-in user's friend list
     *
     * @param currentUserId The user ID to check
     * @param myUserId The logged-in user's ID
     * @return User info with friendship status
     */
    @PostMapping("/attendanceList")
    public R<Map<String, Object>> checkUserFriendship(@RequestBody Map<String, Integer> params) {
        Integer eventId = params.get("eventId");
        Integer myUserId = params.get("myUserId");

        if (eventId == null || myUserId == null) {
            return R.error("Missing required parameters");
        }

        Map<String, Object> result = eventService.checkUserFriendship(eventId, myUserId);

        if (result == null) {
            return R.error("User does not exist");
        }

        return R.success(result);
    }



}
