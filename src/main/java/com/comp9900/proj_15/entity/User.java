package com.comp9900.proj_15.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author comp9900_proj15
 * @since 2025-03-21
 */
@Getter
@Setter
@TableName("User")
@ApiModel(value = "User", description = "")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("Name")
    private String name;

    @TableField("Level_of_study")
    private String levelOfStudy;

    @TableField("Email")
    private String email;

    @TableField("Password_hash")
    private String passwordHash;

    @TableField("Created_at")
    private LocalDateTime createdAt;

    @TableField("User_Type")
    private String userType;

    @TableField("User_city")
    private String userCity;

    @TableField("User_country")
    private String userCountry;

    @TableField("User_Field")
    private String userField;

    @TableField("User_Language")
    private String userLanguage;

    @TableField("User_Regions")
    private String userRegions;

    @TableField("User_Uni")
    private String userUni;

    @TableField("email_verified")
    private Integer emailVerified;

    @TableField("User_icon")
    private String userIcon;

    @TableField("coin")
    private Integer coin;



    public static final String ID = "ID";

    public static final String NAME = "Name";

    public static final String LEVEL_OF_STUDY = "Level_of_study";

    public static final String EMAIL = "Email";

    public static final String PASSWORD_HASH = "Password_hash";

    public static final String CREATED_AT = "Created_at";

    public static final String USER_TYPE = "User_Type";

    public static final String USER_CITY = "User_city";

    public static final String USER_COUNTRY = "User_country";

    public static final String USER_FIELD = "User_Field";

    public static final String USER_LANGUAGE = "User_Language";

    public static final String USER_REGIONS = "User_Regions";

    public static final String USER_UNI = "User_Uni";

    public static final String EMAIL_VERIFIED = "email_verified";

    public static final String USER_ICON = "User_icon";

    public static final String COIN = "coin";

    

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
