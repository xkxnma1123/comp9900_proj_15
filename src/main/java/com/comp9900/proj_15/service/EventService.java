package com.comp9900.proj_15.service;

import com.comp9900.proj_15.entity.Event;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
public interface EventService extends IService<Event> {
    /**
     * 获取活动列表
     * @return 活动列表
     */
    List<Event> getActivityList();

    /**
     * 分页获取活动列表
     * @param page 分页参数
     * @return 分页活动列表
     */
    IPage<Event> getActivityListPage(Page<Event> page);

    /**
     * 根据ID获取活动详情
     * @param id 活动ID
     * @return 活动详情
     */
    Event getActivityById(Long id);
}
