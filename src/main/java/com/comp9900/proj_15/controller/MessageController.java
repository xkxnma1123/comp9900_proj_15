package com.comp9900.proj_15.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.comp9900.proj_15.entity.Message;
import com.comp9900.proj_15.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Frontend Controller
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    @ApiOperation("Send Message")
    //@PreAuthorize("authentication.principal.username == #message.senderId.toString() or hasRole('ADMIN')")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        Message sentMessage = messageService.sendMessage(message);
        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping("/between/{senderId}/{receiverId}")
    @ApiOperation("Get chat history between users")
    //@PreAuthorize("authentication.principal.username == #senderId.toString() or authentication.principal.username == #receiverId.toString() or hasRole('ADMIN')")
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(
            @PathVariable Integer senderId,
            @PathVariable Integer receiverId) {
        List<Message> messages = messageService.getMessagesBetweenUsers(senderId, receiverId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/latest/{userId}")
    @ApiOperation("获取用户与所有联系人的最后一条消息")
    public ResponseEntity<List<Map<String, Object>>> getLatestMessagesWithContacts(
            @PathVariable Integer userId) {
        List<Map<String, Object>> result = messageService.getLatestMessagesWithContacts(userId);
        return ResponseEntity.ok(result);
    }

}
