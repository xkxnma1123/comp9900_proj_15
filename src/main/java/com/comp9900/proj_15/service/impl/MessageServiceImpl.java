package com.comp9900.proj_15.service.impl;

import com.comp9900.proj_15.entity.Message;
import com.comp9900.proj_15.mapper.MessageMapper;
import com.comp9900.proj_15.service.FriendsService;
import com.comp9900.proj_15.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Service Implementation Class
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private FriendsService friendsService; // Inject your friend service

    @Override
    public Message sendMessage(Message message) {
        // Verify if sender and receiver are friends
        if (!areFriends(message.getSenderId(), message.getReceiverId())) {
            throw new RuntimeException("Users are not friends, cannot send messages");
        }

        // Set message creation time
        message.setCreatedAt(LocalDateTime.now());
        message.setRead(null); // Ignore read field

        // Save message to database
        this.save(message);

        // Send message to WebSocket
        messagingTemplate.convertAndSendToUser(
                String.valueOf(message.getReceiverId()),
                "/queue/messages",
                message
        );

        return message;
    }

    @Override
    public List<Message> getMessagesBetweenUsers(Integer senderId, Integer receiverId) {
        // Verify if both users are friends
        if (!areFriends(senderId, receiverId)) {
            throw new RuntimeException("Users are not friends, cannot view chat history");
        }

        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(wrapper ->
                wrapper.and(w ->
                        w.eq(Message::getSenderId, senderId)
                                .eq(Message::getReceiverId, receiverId)
                ).or(w ->
                        w.eq(Message::getSenderId, receiverId)
                                .eq(Message::getReceiverId, senderId)
                )
        ).orderByAsc(Message::getCreatedAt);

        return this.list(queryWrapper);
    }

    // Helper method to verify if two users are friends
    private boolean areFriends(Integer userId1, Integer userId2) {
        List<Map<String, Object>> friends = friendsService.getFriends(userId1);

        for (Map<String, Object> friend : friends) {
            Integer friendId = (Integer) friend.get("Friend_ID"); // Assume the key for friend ID in Map is "userId"
            //String status = (String) friend.get("Status");     // Assume the key for status is "status"

            if (friendId.equals(userId2)) {
                return true;
            }
        }

        return false;
    }

}
