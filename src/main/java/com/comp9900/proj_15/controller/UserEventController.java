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


//    public ResponseEntity<?> attendEvent(@RequestParam("eventId") Long eventId) {
//        try {
//            // 从SecurityContext获取认证信息
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication == null || !authentication.isAuthenticated()) {
//                Map<String, Object> response = new HashMap<>();
//                response.put("success", false);
//                response.put("message", "未认证的请求");
//                return ResponseEntity.status(401).body(response);
//            }
//
//            // 获取当前登录用户的ID (不是邮箱)
//            String userIdStr = authentication.getName();
//            Long userId = Long.valueOf(userIdStr);
//
//            // 调用服务方法参加活动
//            UserEvent userEvent = userEventService.attendEvent(userId, eventId);
//
//            // 构建成功响应
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("message", "成功参加活动");
//            response.put("data", userEvent);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            // 构建错误响应
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", false);
//            response.put("message", e.getMessage());
//
//            return ResponseEntity.badRequest().body(response);
//        }
//    }

    /**
     * 用户参加活动的端点
     * 使用JWT令牌获取当前用户，确保用户只能以自己的身份参加活动
     *
     * @param eventId 活动ID
     * @return 操作结果
     */
    @PostMapping("/attend")
    public R<UserEvent> attendEvent(@RequestBody Map<String, String> params) {
        try {
            // 从参数中获取活动ID和用户ID
            String eventIdStr = params.get("eventId");
            String userIdStr = params.get("userId");

            if (eventIdStr == null || userIdStr == null) {
                return R.error("缺少必要参数：eventId或userId");
            }

            Long eventId = Long.valueOf(eventIdStr);
            Long userId = Long.valueOf(userIdStr);

            // 调用服务方法参加活动
            UserEvent userEvent = userEventService.attendEvent(userId, eventId);

            // 返回成功响应
            return R.success("成功参加活动", userEvent);

        } catch (NumberFormatException e) {
            // 参数格式错误的响应
            return R.error(400, "参数格式错误：eventId和userId必须为数字");
        } catch (Exception e) {
            // 返回错误响应
            return R.error(e.getMessage());
        }
    }

        /**
         * 用户退出活动的端点
         * 仅将用户状态设置为quit，不改变check标志
         *
         * @param eventId 活动ID
         * @return 操作结果
         */
        @PostMapping("/quit")
        public R<UserEvent> quitEvent (@RequestBody Map < String, String > params){
            try {
                // 从参数中获取活动ID和用户ID
                String eventIdStr = params.get("eventId");
                String userIdStr = params.get("userId");

                if (eventIdStr == null || userIdStr == null) {
                    return R.error("缺少必要参数：eventId或userId");
                }

                Long eventId = Long.valueOf(eventIdStr);
                Long userId = Long.valueOf(userIdStr);

                // 调用服务方法退出活动
                UserEvent userEvent = userEventService.quitEvent(userId, eventId);

                // 返回成功响应
                return R.success("成功退出活动", userEvent);

            } catch (NumberFormatException e) {
                // 参数格式错误的响应
                return R.error(400, "参数格式错误：eventId和userId必须为数字");
            } catch (Exception e) {
                // 返回错误响应
                return R.error(e.getMessage());
            }
        }


}
//    public ResponseEntity<?> quitEvent(@RequestParam("eventId") Long eventId) {
//        try {
//            // 从SecurityContext获取认证信息
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication == null || !authentication.isAuthenticated()) {
//                Map<String, Object> response = new HashMap<>();
//                response.put("success", false);
//                response.put("message", "未认证的请求");
//                return ResponseEntity.status(401).body(response);
//            }
//
//            // 获取当前登录用户的ID
//            String userIdStr = authentication.getName();
//            Long userId = Long.valueOf(userIdStr);
//
//            // 调用服务方法退出活动
//            UserEvent userEvent = userEventService.quitEvent(userId, eventId);
//
//            // 构建成功响应
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("message", "成功退出活动");
//            response.put("data", userEvent);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            // 构建错误响应
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", false);
//            response.put("message", e.getMessage());
//
//            return ResponseEntity.badRequest().body(response);
//        }
//    }

