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
}
