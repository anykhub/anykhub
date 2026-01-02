package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Facility;
import com.example.demo.entity.MilitaryStandard;
import com.example.demo.entity.Tag;

import java.util.List;

/**
 * 设施 Service 接口
 */
public interface FacilityService extends IService<Facility> {

    /**
     * 获取设施关联的军标资源
     */
    List<MilitaryStandard> getMilitaryStandardsByFacilityId(Long facilityId);

    /**
     * 获取设施关联的标签
     */
    List<Tag> getTagsByFacilityId(Long facilityId);

    /**
     * 绑定军标资源
     */
    boolean bindMilitaryStandards(Long facilityId, List<Long> msIds);

    /**
     * 解绑军标资源
     */
    boolean unbindMilitaryStandards(Long facilityId, List<Long> msIds);

    /**
     * 绑定标签
     */
    boolean bindTags(Long facilityId, List<Long> tagIds);

    /**
     * 解绑标签
     */
    boolean unbindTags(Long facilityId, List<Long> tagIds);

    /**
     * 构建设施树（包含军标和标签信息）
     */
    List<com.example.demo.vo.FacilityTreeNode> buildFacilityTree(Long parentId);

    /**
     * 按国家查询设施树
     */
    List<com.example.demo.vo.CountryFacilityTree> getFacilityTreeByCountry();
}
