package com.example.demo.vo;

import com.example.demo.entity.MilitaryStandard;
import com.example.demo.entity.Tag;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 设施树节点 VO
 * 用于树形结构展示，包含设施基本信息、子设施、军标资源、标签
 */
@Data
public class FacilityTreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设施ID
     */
    private Long facilityId;

    /**
     * 设施编码
     */
    private String facilityCode;

    /**
     * 设施名称
     */
    private String facilityName;

    /**
     * 父设施ID
     */
    private Long parentFacilityId;

    /**
     * 所属页签ID
     */
    private Long tabId;

    /**
     * 页签名称
     */
    private String tabName;

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 设施类型
     */
    private String facilityType;

    /**
     * 位置
     */
    private String location;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 面积
     */
    private BigDecimal area;

    /**
     * 容量
     */
    private Integer capacity;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 关联的军标资源列表
     */
    private List<MilitaryStandard> militaryStandards = new ArrayList<>();

    /**
     * 关联的标签列表
     */
    private List<Tag> tags = new ArrayList<>();

    /**
     * 子设施列表（递归结构）
     */
    private List<FacilityTreeNode> children = new ArrayList<>();

    /**
     * 是否有子节点
     */
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    /**
     * 添加子节点
     */
    public void addChild(FacilityTreeNode child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }
}
