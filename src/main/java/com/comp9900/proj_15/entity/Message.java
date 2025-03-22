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
@TableName("Message")
@ApiModel(value = "Message对象", description = "")
public class Message extends Model<Message> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "MSG_ID", type = IdType.AUTO)
    private Integer msgId;

    @TableField("sender_ID")
    private Integer senderId;

    @TableField("receiver_ID")
    private Integer receiverId;

    @TableField("content")
    private String content;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("read")
    private Boolean read;

    public static final String MSG_ID = "MSG_ID";

    public static final String SENDER_ID = "sender_ID";

    public static final String RECEIVER_ID = "receiver_ID";

    public static final String CONTENT = "content";

    public static final String CREATED_AT = "created_at";

    public static final String READ = "read";

    @Override
    public Serializable pkVal() {
        return this.msgId;
    }
}
