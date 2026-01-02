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
 * 军标资源表
 */
@Data
@TableName("MILITARY_STANDARD")
public class MilitaryStandard implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 军标ID
     */
    @TableId(value = "MS_ID", type = IdType.AUTO)
    private Long msId;

    /**
     * 军标编码
     */
    @TableField("MS_CODE")
    private String msCode;

    /**
     * 军标名称
     */
    @TableField("MS_NAME")
    private String msName;

    /**
     * 军标分类
     */
    @TableField("MS_CATEGORY")
    private String msCategory;

    /**
     * 版本号
     */
    @TableField("MS_VERSION")
    private String msVersion;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 文件地址
     */
    @TableField("FILE_URL")
    private String fileUrl;

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
