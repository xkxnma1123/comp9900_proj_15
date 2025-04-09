package com.comp9900.proj_15.service;

import com.comp9900.proj_15.entity.UserEvent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  Service Class
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
public interface UserEventService extends IService<UserEvent> {
    /**
     * 用户参加活动
     *
     * @param userId 用户ID
     * @param eventId 活动ID
     * @return 创建的UserEvent记录
     */
    UserEvent attendEvent(Long userId, Long eventId);

    /**
     * 用户退出活动
     * 仅更改状态为quit，不改变check标志
     *
     * @param userId 用户ID
     * @param eventId 活动ID
     * @return 更新后的UserEvent对象
     */
    UserEvent quitEvent(Long userId, Long eventId);
}
