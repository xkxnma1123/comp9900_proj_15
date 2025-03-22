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
@TableName("Event")
@ApiModel(value = "Event对象", description = "")
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

    public static final String ID = "ID";

    public static final String TITLE = "Title";

    public static final String DATE = "Date";

    public static final String DESCRIPTION = "Description";

    public static final String IMG = "IMG";

    public static final String EXTERNAL_LINK = "External_Link";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
