package com.comp9900.proj_15.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.comp9900.proj_15.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.time.LocalDateTime;
import java.util.Map;

import java.util.List;


/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {



    Map<String, Object> findUserById(Long id);
    
    /**
     * 通过邮箱检查用户是否存在
     */
    @Select("SELECT COUNT(*) FROM User WHERE email = #{email}")
    Long countByEmail(@Param("email") String email);
    
    /**
     * 直接插入用户数据
     */
    @Insert("INSERT INTO User(name, email, level_of_study, password_hash, created_at, user_type, user_country, user_regions, user_city, user_field, user_uni, user_language) " +
            "VALUES(#{name}, #{email}, #{levelOfStudy}, #{passwordHash}, #{createdAt}, #{userType}, #{userCountry}, #{userRegions}, #{userCity}, #{userField}, #{userUni}, #{userLanguage})")
    void insertUser(@Param("name") String name, 
                   @Param("email") String email, 
                   @Param("levelOfStudy") String levelOfStudy, 
                   @Param("passwordHash") String passwordHash,
                   @Param("createdAt") LocalDateTime createdAt,
                   @Param("userType") String userType,
                    @Param("userCountry") String userCountry,
                    @Param("userRegions") String userRegions,
                    @Param("userCity") String userCity,
                    @Param("userField") String userField,
                    @Param("userUni") String userUni,
                    @Param("userLanguage") String userLanguage);
    
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
<<<<<<< Updated upstream
     * 根据ID检查用户是否存在
     */
    @Select("SELECT COUNT(*) FROM User WHERE ID = #{id}")
    Long countById(@Param("id") Integer id);


//     * 根据条件查询用户ID列表
//     *
//     * @param field 专业领域
//     * @param university 大学
//     * @param city 城市
//     * @return 用户ID列表
//     */
    @Select("<script>"
            + "SELECT ID FROM User WHERE 1=1"
            + "<if test='field != null and field != \"\"'> AND User_Field = #{field}</if>"
            + "<if test='university != null and university != \"\"'> AND User_Uni = #{university}</if>"
            + "<if test='city != null and city != \"\"'> AND User_city = #{city}</if>"
            + "</script>")
    List<Integer> selectIdsByCondition(@Param("field") String field,
                                       @Param("university") String university,
                                       @Param("city") String city);

    /**
     * 根据ID列表查询用户
     *
     * @param ids ID列表
     * @return 用户列表
     */
    @Select("<script>"
            + "SELECT * FROM User WHERE ID IN "
            + "<foreach collection='ids' item='id' open='(' separator=',' close=')'>"
            + "#{id}"
            + "</foreach>"
            + "</script>")
    List<User> selectByIds(@Param("ids") List<Integer> ids);

}

