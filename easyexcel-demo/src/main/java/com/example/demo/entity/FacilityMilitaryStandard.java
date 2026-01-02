package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设施-军标资源关联表
 */
@Data
@TableName("FACILITY_MILITARY_STANDARD")
public class FacilityMilitaryStandard implements Serializable {

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
     * 军标资源ID
     */
    @TableField("MS_ID")
    private Long msId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;
}
