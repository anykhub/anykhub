package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.FacilityTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设施-标签关联 Mapper 接口
 */
@Mapper
public interface FacilityTagMapper extends BaseMapper<FacilityTag> {
}
