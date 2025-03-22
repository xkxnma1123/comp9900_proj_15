package com.comp9900.proj_15.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
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
@TableName("Friends")
@ApiModel(value = "Friends对象", description = "")
public class Friends extends Model<Friends> {

    private static final long serialVersionUID = 1L;

    @TableId("UID")
    private Integer uid;

    @TableField("Friend_ID")
    private Integer friendId;

    @TableField("Status")
    private String status;

    public static final String UID = "UID";

    public static final String FRIEND_ID = "Friend_ID";

    public static final String STATUS = "Status";

    @Override
    public Serializable pkVal() {
        return this.friendId;
    }
}
