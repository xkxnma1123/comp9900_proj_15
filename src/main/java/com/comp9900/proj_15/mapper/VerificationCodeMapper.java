package com.comp9900.proj_15.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.comp9900.proj_15.entity.VerificationCode;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface VerificationCodeMapper extends BaseMapper<VerificationCode> {
    

    /**
     * Find the latest unused verification code for the specified email
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
     * make the specified verification code as used
     */
    @Update("UPDATE Verification_Code SET used = 1 WHERE id = #{id}")
    int markAsUsed(@Param("id") Integer id);
    
    /**
     * invalidate all verification codes for the specified email
     */
    @Update("UPDATE Verification_Code SET used = 1 WHERE email = #{email} AND used = 0")
    int invalidateAllCodesForEmail(@Param("email") String email);
    
    /**
     * clean up all used and expired verification codes
     */
    @Delete("DELETE FROM Verification_Code WHERE used = 1 OR expiry_date < NOW()")
    int deleteUsedAndExpiredCodes();
}