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
 * 设施页签表
 */
@Data
@TableName("FACILITY_TAB")
public class FacilityTab implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页签ID
     */
    @TableId(value = "TAB_ID", type = IdType.AUTO)
    private Long tabId;

    /**
     * 页签编码
     */
    @TableField("TAB_CODE")
    private String tabCode;

    /**
     * 页签名称
     */
    @TableField("TAB_NAME")
    private String tabName;

    /**
     * 页签图标
     */
    @TableField("TAB_ICON")
    private String tabIcon;

    /**
     * 排序
     */
    @TableField("TAB_ORDER")
    private Integer tabOrder;

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
