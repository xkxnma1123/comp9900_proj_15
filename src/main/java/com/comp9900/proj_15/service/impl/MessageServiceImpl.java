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
 *  服务实现类
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
    private FriendsService friendsService; // 注入你的好友服务


    @Override
    public Message sendMessage(Message message) {
        // 验证发送者和接收者是否为好友
        if (!areFriends(message.getSenderId(), message.getReceiverId())) {
            throw new RuntimeException("用户不是好友关系，无法发送消息");
        }

        // 设置消息创建时间
        message.setCreatedAt(LocalDateTime.now());
        message.setRead(null); // 忽略已读字段

        // 保存消息到数据库
        this.save(message);

        // 发送消息到WebSocket
        messagingTemplate.convertAndSendToUser(
                String.valueOf(message.getReceiverId()),
                "/queue/messages",
                message
        );

        return message;
    }



    @Override
    public List<Message> getMessagesBetweenUsers(Integer senderId, Integer receiverId) {
        // 验证两用户是否为好友
        if (!areFriends(senderId, receiverId)) {
            throw new RuntimeException("用户不是好友关系，无法查看聊天记录");
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

    // 验证两个用户是否为好友关系的辅助方法
    private boolean areFriends(Integer userId1, Integer userId2) {
        List<Map<String, Object>> friends = friendsService.getFriends(userId1);

        for (Map<String, Object> friend : friends) {
            Integer friendId = (Integer) friend.get("Friend_ID"); // 假设Map中存储的好友ID的键为"userId"
            //String status = (String) friend.get("Status");     // 假设状态的键为"status"

            if (friendId.equals(userId2)) {
                return true;
            }
        }

        return false;
    }


}
