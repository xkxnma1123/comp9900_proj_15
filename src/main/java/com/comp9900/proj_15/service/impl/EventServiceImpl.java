package com.comp9900.proj_15.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.comp9900.proj_15.entity.Event;
import com.comp9900.proj_15.mapper.EventMapper;
import com.comp9900.proj_15.service.EventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


import java.util.List;

import java.util.List;

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
}
