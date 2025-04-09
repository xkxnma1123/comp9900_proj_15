package com.comp9900.proj_15.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.comp9900.proj_15.entity.VerificationCode;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface VerificationCodeMapper extends BaseMapper<VerificationCode> {
    
    /**
     * 插入验证码记录
     */
    // @Insert("INSERT INTO Verification_Code(email, code, expiry_date, used) VALUES(#{email}, #{code}, #{expiryDate}, #{used})")
    // @Options(useGeneratedKeys = true, keyProperty = "id")
    // int insertVerificationCode(@Param("email") String email, @Param("code") String code, 
    //                           @Param("expiryDate") LocalDateTime expiryDate, @Param("used") int used);
 
    /**
     * 查找指定邮箱最新的未使用验证码
     */
    @Select("SELECT * FROM Verification_Code WHERE email = #{email} AND used = 0 ORDER BY id DESC LIMIT 1")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "email", column = "email"),
        @Result(property = "code", column = "code"),
        @Result(property = "expiryDate", column = "expiry_date"),
        @Result(property = "used", column = "used"),
        @Result(property = "createdAt", column = "created_at")
    })
    VerificationCode findLatestByEmail(@Param("email") String email);
    
    /**
     * 将验证码标记为已使用
     */
    @Update("UPDATE Verification_Code SET used = 1 WHERE id = #{id}")
    int markAsUsed(@Param("id") Integer id);
    
    /**
     * 使指定邮箱的所有未使用验证码失效
     */
    @Update("UPDATE Verification_Code SET used = 1 WHERE email = #{email} AND used = 0")
    int invalidateAllCodesForEmail(@Param("email") String email);
    
    /**
     * 清理已使用和过期的验证码
     */
    @Delete("DELETE FROM Verification_Code WHERE used = 1 OR expiry_date < NOW()")
    int deleteUsedAndExpiredCodes();
}