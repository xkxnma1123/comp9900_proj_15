package com.comp9900.proj_15.service;

import com.comp9900.proj_15.entity.Event;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import java.util.List;

/**
 * <p>
 *  Service Class
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
public interface EventService extends IService<Event> {
    /**
     * Get event list
     * @return Event list
     */
    List<Event> getActivityList();

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
}
