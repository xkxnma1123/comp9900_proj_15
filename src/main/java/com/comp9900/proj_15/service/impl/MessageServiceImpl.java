package com.comp9900.proj_15.service.impl;

import com.comp9900.proj_15.entity.Message;
import com.comp9900.proj_15.mapper.MessageMapper;
import com.comp9900.proj_15.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
