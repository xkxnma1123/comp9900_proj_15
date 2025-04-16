package com.comp9900.proj_15.mapper;

import com.comp9900.proj_15.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<Map<String, Object>> findLatestMessagesWithContacts(@Param("userId") Integer userId);

}
