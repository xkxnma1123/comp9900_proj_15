package com.comp9900.proj_15.service.impl;

import com.comp9900.proj_15.entity.Message;
import com.comp9900.proj_15.entity.User;
import com.comp9900.proj_15.mapper.MessageMapper;
import com.comp9900.proj_15.service.FriendsService;
import com.comp9900.proj_15.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.comp9900.proj_15.service.UserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.Timestamp;

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

    @Autowired
    private UserService userService;

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


    @Override
    public List<Map<String, Object>> getLatestMessagesWithContacts(Integer userId) {
        // 1. 查询该用户与所有联系人的对话，并按联系人分组获取最后一条消息
        List<Map<String, Object>> contactsWithLastMessage = this.baseMapper.findLatestMessagesWithContacts(userId);

        // 2. 构建返回结果
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> item : contactsWithLastMessage) {
            Number contactIdNumber = (Number) item.get("contactId");
            Integer contactId = contactIdNumber != null ? contactIdNumber.intValue() : null;

            Message lastMessage = new Message();

            lastMessage.setMsgId((Integer) item.get("msgId"));
            lastMessage.setSenderId((Integer) item.get("senderId"));
            lastMessage.setReceiverId((Integer) item.get("receiverId"));
            lastMessage.setContent((String) item.get("content"));

            // 安全转换 created_at 字段
            Object createdAtObj = item.get("createdAt");
            if (createdAtObj instanceof Timestamp) {
                lastMessage.setCreatedAt(((Timestamp) createdAtObj).toLocalDateTime());
            } else if (createdAtObj instanceof LocalDateTime) {
                lastMessage.setCreatedAt((LocalDateTime) createdAtObj);
            } else {
                lastMessage.setCreatedAt(null); // 或者打个日志警告
            }

            lastMessage.setRead((Boolean) item.get("read"));

            User contactUser = userService.getUserById(contactId);
            if (contactUser != null) {
                Map<String, Object> contactMap = new HashMap<>();
                contactMap.put("contactId", contactId);
                contactMap.put("contactName", contactUser.getName());
                contactMap.put("contactAvatar", contactUser.getUserIcon());
                contactMap.put("lastMessage", lastMessage);
                contactMap.put("lastMessageTime", lastMessage.getCreatedAt());

                result.add(contactMap);
            }
        }

//        for (Map<String, Object> item : contactsWithLastMessage) {
//            //Integer contactId = (Integer) item.get("contactId");
//            Number contactIdNumber = (Number) item.get("contactId");
//            Integer contactId = contactIdNumber != null ? contactIdNumber.intValue() : null;
//            Message lastMessage = new Message();
//
//            // 从查询结果中构建Message对象
//            lastMessage.setMsgId((Integer) item.get("msgId"));
//            lastMessage.setSenderId((Integer) item.get("senderId"));
//            lastMessage.setReceiverId((Integer) item.get("receiverId"));
//            lastMessage.setContent((String) item.get("content"));
//            //lastMessage.setCreatedAt((LocalDateTime) item.get("createdAt"));
//            Timestamp ts = (Timestamp) item.get("created_at");
//            LocalDateTime createdAt = ts.toLocalDateTime();
//            lastMessage.setRead((Boolean) item.get("read"));
//
//            // 获取联系人信息
//            User contactUser = userService.getUserById(contactId);
//
//            if (contactUser != null) {
//                Map<String, Object> contactMap = new HashMap<>();
//                contactMap.put("contactId", contactId);
//                contactMap.put("contactName", contactUser.getName());
//                contactMap.put("contactAvatar", contactUser.getUserIcon());
//                contactMap.put("lastMessage", lastMessage);
//                contactMap.put("lastMessageTime", lastMessage.getCreatedAt());
//
//                result.add(contactMap);
//            }
//        }

        // 3. 按最后消息时间降序排序
        result.sort((a, b) -> ((LocalDateTime)b.get("lastMessageTime")).compareTo((LocalDateTime)a.get("lastMessageTime")));

        return result;
    }

}
