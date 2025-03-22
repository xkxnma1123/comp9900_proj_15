package com.comp9900.proj_15.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.comp9900.proj_15.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 通过邮箱检查用户是否存在
     */
    @Select("SELECT COUNT(*) FROM User WHERE email = #{email}")
    Long countByEmail(@Param("email") String email);
    
    /**
     * 直接插入用户数据
     */
    @Insert("INSERT INTO User(name, email, level_of_study, password_hash, created_at, user_type) " +
            "VALUES(#{name}, #{email}, #{levelOfStudy}, #{passwordHash}, #{createdAt}, #{userType})")
    void insertUser(@Param("name") String name, 
                   @Param("email") String email, 
                   @Param("levelOfStudy") String levelOfStudy, 
                   @Param("passwordHash") String passwordHash,
                   @Param("createdAt") LocalDateTime createdAt,
                   @Param("userType") String userType);
    
    /**
     * 查询用户信息但不包含密码
     */
    @Select("SELECT id, name, email, level_of_study, created_at, user_type, user_city, user_country, " +
            "user_field, user_language, user_regions, user_uni " +
            "FROM User WHERE email = #{email}")
    Map<String, Object> findUserByEmail(@Param("email") String email);
    
    /**
     * 获取用户密码用于验证
     */
    @Select("SELECT password_hash FROM User WHERE email = #{email}")
    String getPasswordByEmail(@Param("email") String email);

    /**
     * 根据ID检查用户是否存在
     */
    @Select("SELECT COUNT(*) FROM User WHERE ID = #{id}")
    Long countById(@Param("id") Integer id);
}