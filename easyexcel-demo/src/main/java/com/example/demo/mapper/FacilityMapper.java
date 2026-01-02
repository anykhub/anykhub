package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Facility;
import com.example.demo.entity.MilitaryStandard;
import com.example.demo.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 设施 Mapper 接口
 */
@Mapper
public interface FacilityMapper extends BaseMapper<Facility> {

    /**
     * 查询设施关联的军标资源
     */
    @Select("SELECT ms.* FROM MILITARY_STANDARD ms " +
            "INNER JOIN FACILITY_MILITARY_STANDARD fms ON ms.MS_ID = fms.MS_ID " +
            "WHERE fms.FACILITY_ID = #{facilityId} AND ms.DEL_FLAG = '0'")
    List<MilitaryStandard> selectMilitaryStandardsByFacilityId(@Param("facilityId") Long facilityId);

    /**
     * 查询设施关联的标签
     */
    @Select("SELECT t.* FROM TAG t " +
            "INNER JOIN FACILITY_TAG ft ON t.TAG_ID = ft.TAG_ID " +
            "WHERE ft.FACILITY_ID = #{facilityId} AND t.DEL_FLAG = '0'")
    List<Tag> selectTagsByFacilityId(@Param("facilityId") Long facilityId);
}
