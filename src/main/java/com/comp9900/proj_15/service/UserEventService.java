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
     * user attend event
     *
     * @param userId 
     * @param eventId 
     * @return created UserEvent object
     */
    UserEvent attendEvent(Long userId, Long eventId);

    /**
     * quit event
     * only change the status to "quit", keep the check flag unchanged
     *
     * @param userId 
     * @param eventId 
     * @return updated UserEvent object
     */
    UserEvent quitEvent(Long userId, Long eventId);
}
