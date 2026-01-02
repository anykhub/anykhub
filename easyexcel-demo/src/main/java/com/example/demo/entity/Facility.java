package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设施表
 */
@Data
@TableName("FACILITY")
public class Facility implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设施ID
     */
    @TableId(value = "FACILITY_ID", type = IdType.AUTO)
    private Long facilityId;

    /**
     * 设施编码
     */
    @TableField("FACILITY_CODE")
    private String facilityCode;

    /**
     * 设施名称
     */
    @TableField("FACILITY_NAME")
    private String facilityName;

    /**
     * 所属页签ID
     */
    @TableField("TAB_ID")
    private Long tabId;

    /**
     * 设施类型
     */
    @TableField("FACILITY_TYPE")
    private String facilityType;

    /**
     * 位置
     */
    @TableField("LOCATION")
    private String location;

    /**
     * 纬度
     */
    @TableField("LATITUDE")
    private BigDecimal latitude;

    /**
     * 经度
     */
    @TableField("LONGITUDE")
    private BigDecimal longitude;

    /**
     * 面积
     */
    @TableField("AREA")
    private BigDecimal area;

    /**
     * 容量
     */
    @TableField("CAPACITY")
    private Integer capacity;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

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
