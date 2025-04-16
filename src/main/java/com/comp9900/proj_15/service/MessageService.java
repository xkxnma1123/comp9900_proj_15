package com.comp9900.proj_15.service;

import com.comp9900.proj_15.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Service Class
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
public interface MessageService extends IService<Message> {
    /**
     * Send message
     * @param message Message object
     * @return Saved message
     */
    Message sendMessage(Message message);

    /**
     * Get chat history between two users
     * @param senderId Sender ID
     * @param receiverId Receiver ID
     * @return Message list
     */
    List<Message> getMessagesBetweenUsers(Integer senderId, Integer receiverId);

    List<Map<String, Object>> getLatestMessagesWithContacts(Integer userId);
}
