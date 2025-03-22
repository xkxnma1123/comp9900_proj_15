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
 *  前端控制器
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
     * 获取活动列表
     * @return 活动列表
     */
    @GetMapping("/list")
    public R<List<Event>> getActivityList() {
        List<Event> list = eventService.getActivityList();
        return R.success(list);
    }

    /**
     * 分页获取活动列表
     * @param current 当前页
     * @param size 每页大小
     * @return 分页活动列表
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
     * 获取活动详情
     * @param id 活动ID
     * @return 活动详情
     */
    @GetMapping("/detail/{id}")
    public R<Event> getActivityDetail(@PathVariable Long id) {
        Event activity = eventService.getActivityById(id);
        if (activity == null) {
            return R.error("活动不存在");
        }
        return R.success(activity);
    }
}
