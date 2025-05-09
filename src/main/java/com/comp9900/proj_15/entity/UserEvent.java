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
@TableName("User_Event")
@ApiModel(value = "UserEvent", description = "")
public class UserEvent extends Model<UserEvent> {

    private static final long serialVersionUID = 1L;

    @TableId("txn_id")
    private Integer txnID;

    @TableField("EID")
    private Integer eid;


    @TableField("UID")
    private Integer uid;


    @TableField("Status")
    private String status;

    @TableField("check_flag")
    private boolean checkFlag;



    public static final String UID = "UID";

    public static final String EID = "EID";

    public static final String STATUS = "Status";


    @Override
    public Serializable pkVal() {
        return this.eid;
    }
}
