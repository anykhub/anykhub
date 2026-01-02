package com.example.demo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 国家设施树 VO
 * 按国家分组的设施树形结构
 */
@Data
public class CountryFacilityTree implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 该国家的设施总数
     */
    private Integer totalFacilities;

    /**
     * 该国家的一级设施列表（根节点）
     */
    private List<FacilityTreeNode> facilities = new ArrayList<>();

    /**
     * 添加设施节点
     */
    public void addFacility(FacilityTreeNode facility) {
        if (this.facilities == null) {
            this.facilities = new ArrayList<>();
        }
        this.facilities.add(facility);
    }
}
