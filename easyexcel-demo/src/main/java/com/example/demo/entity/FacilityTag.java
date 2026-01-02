package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设施-标签关联表
 */
@Data
@TableName("FACILITY_TAG")
public class FacilityTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 设施ID
     */
    @TableField("FACILITY_ID")
    private Long facilityId;

    /**
     * 标签ID
     */
    @TableField("TAG_ID")
    private Long tagId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;
}
