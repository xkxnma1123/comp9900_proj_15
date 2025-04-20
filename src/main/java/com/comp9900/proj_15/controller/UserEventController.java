package com.comp9900.proj_15.controller;

import com.comp9900.proj_15.common.R;
import com.comp9900.proj_15.service.EventService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comp9900.proj_15.entity.User;
import com.comp9900.proj_15.entity.UserEvent;
import com.comp9900.proj_15.service.UserEventService;
import com.comp9900.proj_15.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
@RestController
@RequestMapping("/userEvent")
public class UserEventController {

    @Autowired
    private UserEventService userEventService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;



    /**
     * user attend event endpoint
     * This endpoint allows a user to attend an event.
     * It uses the JWT token to get the current user, ensuring that the user can only attend the event in their own identity.
     * 
     *
     * @param eventId event ID
     * @return operation result
     */
    @PostMapping("/attend")
    public R<UserEvent> attendEvent(@RequestBody Map<String, String> params) {
        try {
            // get eventId and userId from params
            String eventIdStr = params.get("eventId");
            String userIdStr = params.get("userId");

            if (eventIdStr == null || userIdStr == null) {
                return R.error("missing required parameters: eventId or userId");
            }

            Long eventId = Long.valueOf(eventIdStr);
            Long userId = Long.valueOf(userIdStr);

            // check if the user is already attending the event
            UserEvent userEvent = userEventService.attendEvent(userId, eventId);

            return R.success("attend event successfully", userEvent);

        } catch (NumberFormatException e) {
            return R.error(400, "parameter format error: eventId and userId must be numbers");
        } catch (Exception e) {
            // return error response
            return R.error(e.getMessage());
        }
    }

        /**
         * user quit event endpoint
         * only change the status to "quit", do not change the check flag
         *
         * @param eventId event ID
         * @return operation result
         */
        @PostMapping("/quit")
        public R<UserEvent> quitEvent (@RequestBody Map < String, String > params){
            try {
                // get eventId and userId from params
                String eventIdStr = params.get("eventId");
                String userIdStr = params.get("userId");

                if (eventIdStr == null || userIdStr == null) {
                    return R.error("missing required parameters: eventId or userId");
                }

                Long eventId = Long.valueOf(eventIdStr);
                Long userId = Long.valueOf(userIdStr);

                // call the service method to quit the event
                UserEvent userEvent = userEventService.quitEvent(userId, eventId);

                return R.success("quit event successfully", userEvent);

            } catch (NumberFormatException e) {
                // parameter format error
                return R.error(400, "parameter format error: eventId and userId must be numbers");
            } catch (Exception e) {
                // return error response
                return R.error(e.getMessage());
            }
        }


}


