package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 标签表
 */
@Data
@TableName("TAG")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签ID
     */
    @TableId(value = "TAG_ID", type = IdType.AUTO)
    private Long tagId;

    /**
     * 标签编码
     */
    @TableField("TAG_CODE")
    private String tagCode;

    /**
     * 标签名称
     */
    @TableField("TAG_NAME")
    private String tagName;

    /**
     * 标签颜色
     */
    @TableField("TAG_COLOR")
    private String tagColor;

    /**
     * 标签类型
     */
    @TableField("TAG_TYPE")
    private String tagType;

    /**
     * 排序
     */
    @TableField("TAG_ORDER")
    private Integer tagOrder;

    /**
     * 状态（0正常 1停用）
     */
    @TableField("STATUS")
    private String status;

    /**
     * 删除标志（0存在 2删除）
     */
    @TableLogic
    @TableField("DEL_FLAG")
    private String delFlag;

    /**
     * 创建者
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}
