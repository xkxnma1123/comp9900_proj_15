package com.comp9900.proj_15.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.comp9900.proj_15.entity.Friends;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface FriendsMapper extends BaseMapper<Friends> {
    
    /**
     * 获取好友请求列表
     */
    @Select("SELECT f.UID, f.Friend_ID, f.Status, u.name as friend_name, u.email as friend_email " +
            "FROM Friends f JOIN User u ON f.UID = u.ID " +
            "WHERE f.Friend_ID = #{userId} AND f.Status = 'pending'")
    List<Map<String, Object>> getFriendRequests(@Param("userId") Integer userId);
    
    /**
     * 获取所有好友
     */
    @Select("SELECT f.UID, f.Friend_ID, u.name as friend_name, u.email as friend_email " +
            "FROM Friends f JOIN User u ON f.Friend_ID = u.ID " +
            "WHERE f.UID = #{userId} AND f.Status = 'accept'")
    List<Map<String, Object>> getFriends(@Param("userId") Integer userId);
    
    /**
     * 检查好友请求是否已存在
     */
    @Select("SELECT COUNT(*) FROM Friends WHERE UID = #{userId} AND Friend_ID = #{friendId}")
    Long checkFriendRequestExists(@Param("userId") Integer userId, @Param("friendId") Integer friendId);
    
    /**
     * 添加好友请求
     */
    @Insert("INSERT INTO Friends(UID, Friend_ID, Status) VALUES(#{userId}, #{friendId}, 'pending')")
    void addFriendRequest(@Param("userId") Integer userId, @Param("friendId") Integer friendId);
    
    /**
     * 更新好友请求状态
     */
    @Update("UPDATE Friends SET Status = #{status} WHERE UID = #{userId} AND Friend_ID = #{friendId}")
    void updateFriendRequestStatus(@Param("userId") Integer userId, @Param("friendId") Integer friendId, @Param("status") String status);

    /**
     * 添加双向好友关系
     */
    @Insert("INSERT INTO Friends(UID, Friend_ID, Status) VALUES(#{friendId}, #{userId}, 'accept')")
    void addReverseFriend(@Param("userId") Integer userId, @Param("friendId") Integer friendId);
    
    /**
     * 获取好友请求详情
     */
    @Select("SELECT * FROM Friends WHERE UID = #{userId} AND Friend_ID = #{friendId}")
    Friends getFriendRequest(@Param("userId") Integer userId, @Param("friendId") Integer friendId);
}