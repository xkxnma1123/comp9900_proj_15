package com.comp9900.proj_15.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@TableName("Event")
@ApiModel(value = "Event", description = "")
public class Event extends Model<Event> {


    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("Title")
    private String title;

    @TableField("Date")
    private LocalDateTime date;

    @TableField("Description")
    private String description;

    @TableField("IMG")
    private byte[] img;

    @TableField("External_Link")
    private String externalLink;

    @TableField("coin")
    private Integer coin;

    @TableField(exist = false)  
    @JsonInclude(JsonInclude.Include.NON_NULL)  
    private String status;

    public static final String ID = "ID";

    public static final String TITLE = "Title";

    public static final String DATE = "Date";

    public static final String DESCRIPTION = "Description";

    public static final String IMG = "IMG";

    public static final String EXTERNAL_LINK = "External_Link";

    public static final String COIN = "coin";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
