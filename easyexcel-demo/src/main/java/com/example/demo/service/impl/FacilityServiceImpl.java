package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Facility;
import com.example.demo.entity.FacilityMilitaryStandard;
import com.example.demo.entity.FacilityTag;
import com.example.demo.entity.MilitaryStandard;
import com.example.demo.entity.Tag;
import com.example.demo.mapper.FacilityMapper;
import com.example.demo.mapper.FacilityMilitaryStandardMapper;
import com.example.demo.mapper.FacilityTagMapper;
import com.example.demo.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 设施 Service 实现类
 */
@Service
public class FacilityServiceImpl extends ServiceImpl<FacilityMapper, Facility> implements FacilityService {

    @Autowired
    private FacilityMilitaryStandardMapper facilityMilitaryStandardMapper;

    @Autowired
    private FacilityTagMapper facilityTagMapper;

    @Override
    public List<MilitaryStandard> getMilitaryStandardsByFacilityId(Long facilityId) {
        return baseMapper.selectMilitaryStandardsByFacilityId(facilityId);
    }

    @Override
    public List<Tag> getTagsByFacilityId(Long facilityId) {
        return baseMapper.selectTagsByFacilityId(facilityId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindMilitaryStandards(Long facilityId, List<Long> msIds) {
        if (msIds == null || msIds.isEmpty()) {
            return false;
        }

        for (Long msId : msIds) {
            // 检查是否已存在关联
            QueryWrapper<FacilityMilitaryStandard> queryWrapper = new QueryWrapper<FacilityMilitaryStandard>();
            queryWrapper.eq("FACILITY_ID", facilityId).eq("MS_ID", msId);
            Long count = facilityMilitaryStandardMapper.selectCount(queryWrapper);

            if (count == 0) {
                FacilityMilitaryStandard fms = new FacilityMilitaryStandard();
                fms.setFacilityId(facilityId);
                fms.setMsId(msId);
                fms.setCreateTime(new Date());
                facilityMilitaryStandardMapper.insert(fms);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unbindMilitaryStandards(Long facilityId, List<Long> msIds) {
        if (msIds == null || msIds.isEmpty()) {
            return false;
        }

        QueryWrapper<FacilityMilitaryStandard> queryWrapper = new QueryWrapper<FacilityMilitaryStandard>();
        queryWrapper.eq("FACILITY_ID", facilityId).in("MS_ID", msIds);
        return facilityMilitaryStandardMapper.delete(queryWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindTags(Long facilityId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return false;
        }

        for (Long tagId : tagIds) {
            // 检查是否已存在关联
            QueryWrapper<FacilityTag> queryWrapper = new QueryWrapper<FacilityTag>();
            queryWrapper.eq("FACILITY_ID", facilityId).eq("TAG_ID", tagId);
            Long count = facilityTagMapper.selectCount(queryWrapper);

            if (count == 0) {
                FacilityTag ft = new FacilityTag();
                ft.setFacilityId(facilityId);
                ft.setTagId(tagId);
                ft.setCreateTime(new Date());
                facilityTagMapper.insert(ft);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unbindTags(Long facilityId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return false;
        }

        QueryWrapper<FacilityTag> queryWrapper = new QueryWrapper<FacilityTag>();
        queryWrapper.eq("FACILITY_ID", facilityId).in("TAG_ID", tagIds);
        return facilityTagMapper.delete(queryWrapper) > 0;
    }

    @Override
    public List<com.example.demo.vo.FacilityTreeNode> buildFacilityTree(Long parentId) {
        // 查询指定父节点下的所有设施
        QueryWrapper<Facility> queryWrapper = new QueryWrapper<Facility>();
        if (parentId == null) {
            queryWrapper.isNull("PARENT_FACILITY_ID");
        } else {
            queryWrapper.eq("PARENT_FACILITY_ID", parentId);
        }
        queryWrapper.eq("DEL_FLAG", "0");
        queryWrapper.orderByAsc("FACILITY_CODE");

        List<Facility> facilities = baseMapper.selectList(queryWrapper);
        List<com.example.demo.vo.FacilityTreeNode> treeNodes = new ArrayList<>();

        for (Facility facility : facilities) {
            com.example.demo.vo.FacilityTreeNode node = convertToTreeNode(facility);

            // 递归查询子设施
            List<com.example.demo.vo.FacilityTreeNode> children = buildFacilityTree(facility.getFacilityId());
            node.setChildren(children);

            treeNodes.add(node);
        }

        return treeNodes;
    }

    @Override
    public List<com.example.demo.vo.CountryFacilityTree> getFacilityTreeByCountry() {
        // 查询所有国家代码
        QueryWrapper<Facility> countryQuery = new QueryWrapper<Facility>();
        countryQuery.select("DISTINCT COUNTRY_CODE");
        countryQuery.eq("DEL_FLAG", "0");
        countryQuery.isNotNull("COUNTRY_CODE");
        countryQuery.orderByAsc("COUNTRY_CODE");

        List<Facility> countryList = baseMapper.selectList(countryQuery);
        List<com.example.demo.vo.CountryFacilityTree> result = new ArrayList<>();

        for (Facility f : countryList) {
            String countryCode = f.getCountryCode();
            if (countryCode == null || countryCode.trim().isEmpty()) {
                continue;
            }

            com.example.demo.vo.CountryFacilityTree countryTree = new com.example.demo.vo.CountryFacilityTree();
            countryTree.setCountryCode(countryCode);
            countryTree.setCountryName(getCountryName(countryCode));

            // 查询该国家的所有一级设施（父设施）
            QueryWrapper<Facility> facilityQuery = new QueryWrapper<Facility>();
            facilityQuery.eq("COUNTRY_CODE", countryCode);
            facilityQuery.isNull("PARENT_FACILITY_ID");
            facilityQuery.eq("DEL_FLAG", "0");
            facilityQuery.orderByAsc("FACILITY_CODE");

            List<Facility> rootFacilities = baseMapper.selectList(facilityQuery);
            List<com.example.demo.vo.FacilityTreeNode> treeNodes = new ArrayList<>();

            for (Facility facility : rootFacilities) {
                com.example.demo.vo.FacilityTreeNode node = convertToTreeNode(facility);

                // 递归查询子设施
                List<com.example.demo.vo.FacilityTreeNode> children = buildFacilityTreeForCountry(
                        facility.getFacilityId(), countryCode);
                node.setChildren(children);

                treeNodes.add(node);
            }

            countryTree.setFacilities(treeNodes);
            countryTree.setTotalFacilities(countFacilitiesByCountry(countryCode));

            result.add(countryTree);
        }

        return result;
    }

    /**
     * 递归构建指定国家的设施树
     */
    private List<com.example.demo.vo.FacilityTreeNode> buildFacilityTreeForCountry(Long parentId, String countryCode) {
        QueryWrapper<Facility> queryWrapper = new QueryWrapper<Facility>();
        queryWrapper.eq("PARENT_FACILITY_ID", parentId);
        queryWrapper.eq("COUNTRY_CODE", countryCode);
        queryWrapper.eq("DEL_FLAG", "0");
        queryWrapper.orderByAsc("FACILITY_CODE");

        List<Facility> facilities = baseMapper.selectList(queryWrapper);
        List<com.example.demo.vo.FacilityTreeNode> treeNodes = new ArrayList<>();

        for (Facility facility : facilities) {
            com.example.demo.vo.FacilityTreeNode node = convertToTreeNode(facility);

            // 递归查询子设施
            List<com.example.demo.vo.FacilityTreeNode> children = buildFacilityTreeForCountry(facility.getFacilityId(),
                    countryCode);
            node.setChildren(children);

            treeNodes.add(node);
        }

        return treeNodes;
    }

    /**
     * 将 Facility 实体转换为 TreeNode
     */
    private com.example.demo.vo.FacilityTreeNode convertToTreeNode(Facility facility) {
        com.example.demo.vo.FacilityTreeNode node = new com.example.demo.vo.FacilityTreeNode();
        node.setFacilityId(facility.getFacilityId());
        node.setFacilityCode(facility.getFacilityCode());
        node.setFacilityName(facility.getFacilityName());
        node.setParentFacilityId(facility.getParentFacilityId());
        node.setTabId(facility.getTabId());
        node.setCountryCode(facility.getCountryCode());
        node.setFacilityType(facility.getFacilityType());
        node.setLocation(facility.getLocation());
        node.setLatitude(facility.getLatitude());
        node.setLongitude(facility.getLongitude());
        node.setArea(facility.getArea());
        node.setCapacity(facility.getCapacity());
        node.setDescription(facility.getDescription());
        node.setStatus(facility.getStatus());
        node.setCreateTime(facility.getCreateTime());

        // 查询关联的军标资源
        List<MilitaryStandard> militaryStandards = baseMapper
                .selectMilitaryStandardsByFacilityId(facility.getFacilityId());
        node.setMilitaryStandards(militaryStandards);

        // 查询关联的标签
        List<Tag> tags = baseMapper.selectTagsByFacilityId(facility.getFacilityId());
        node.setTags(tags);

        return node;
    }

    /**
     * 统计指定国家的设施总数
     */
    private Integer countFacilitiesByCountry(String countryCode) {
        QueryWrapper<Facility> queryWrapper = new QueryWrapper<Facility>();
        queryWrapper.eq("COUNTRY_CODE", countryCode);
        queryWrapper.eq("DEL_FLAG", "0");
        return Math.toIntExact(baseMapper.selectCount(queryWrapper));
    }

    /**
     * 获取国家名称（可以从配置或字典表获取）
     */
    private String getCountryName(String countryCode) {
        switch (countryCode) {
            case "CN":
                return "中国";
            case "US":
                return "美国";
            case "JP":
                return "日本";
            case "UK":
                return "英国";
            case "FR":
                return "法国";
            case "DE":
                return "德国";
            default:
                return countryCode;
        }
    }
}
