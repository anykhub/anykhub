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
 * 用户信息表
 */
@Data
@TableName("SYS_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "USER_ID", type = IdType.AUTO)
    private Long userId;

    /**
     * 部门ID
     */
    @TableField("DEPT_ID")
    private Long deptId;

    /**
     * 用户账号
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 用户昵称
     */
    @TableField("NICK_NAME")
    private String nickName;

    /**
     * 用户类型（00系统用户）
     */
    @TableField("USER_TYPE")
    private String userType;

    /**
     * 用户邮箱
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 手机号码
     */
    @TableField("PHONENUMBER")
    private String phonenumber;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @TableField("SEX")
    private String sex;

    /**
     * 头像地址
     */
    @TableField("AVATAR")
    private String avatar;

    /**
     * 密码
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    @TableField("STATUS")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    @TableField("DEL_FLAG")
    private String delFlag;

    /**
     * 最后登录IP
     */
    @TableField("LOGIN_IP")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @TableField("LOGIN_DATE")
    private Date loginDate;

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
