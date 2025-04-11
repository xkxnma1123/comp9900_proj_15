package com.comp9900.proj_15.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comp9900.proj_15.entity.Event;
import com.baomidou.mybatisplus.extension.service.IService;
import com.comp9900.proj_15.entity.UserEvent;

import java.util.List;
import java.util.Map;

/**
 * Service Class
 */
public interface EventService extends IService<Event> {

    /**
     * Get event list
     * @return Event list
     */
    List<Event> getActivityList(Integer userId);

    /**
     * Get paginated event list
     * @param page Pagination parameters
     * @return Paginated event list
     */
    IPage<Event> getActivityListPage(Page<Event> page);

    /**
     * Get event details by ID
     * @param id Event ID
     * @return Event details
     */
    Event getActivityById(Long id);

    Map<String, Object> getActivityWithParticipants(Long id, Integer userId);



    /**
     * Check if a user is in another user's friends list
     *
     * @param currentUserId The user to check
     * @param myUserId The logged-in user's ID
     * @return Map containing user info and friendship status
     */
    Map<String, Object> checkUserFriendship(Integer currentUserId, Integer myUserId);


}
