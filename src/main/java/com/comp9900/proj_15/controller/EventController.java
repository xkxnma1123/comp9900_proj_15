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
     * @return Event list
     */
    @GetMapping("/list")
    public R<List<Event>> getActivityList() {
        List<Event> list = eventService.getActivityList();
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
    @GetMapping("/detail/{id}")
    public R<Event> getActivityDetail(@PathVariable Long id) {
        Event activity = eventService.getActivityById(id);
        if (activity == null) {
            return R.error("Event does not exist");
        }
        return R.success(activity);
    }
}
