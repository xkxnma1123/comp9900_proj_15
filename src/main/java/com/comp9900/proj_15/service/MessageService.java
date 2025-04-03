package com.comp9900.proj_15.service;

import com.comp9900.proj_15.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
public interface MessageService extends IService<Message> {
    /**
     * 发送消息
     * @param message 消息对象
     * @return 已保存的消息
     */
    Message sendMessage(Message message);

    /**
     * 获取两个用户之间的聊天记录
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @return 消息列表
     */
    List<Message> getMessagesBetweenUsers(Integer senderId, Integer receiverId);

}
