package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.FacilityTab;
import com.example.demo.mapper.FacilityTabMapper;
import com.example.demo.service.FacilityTabService;
import org.springframework.stereotype.Service;

/**
 * 设施页签 Service 实现类
 */
@Service
public class FacilityTabServiceImpl extends ServiceImpl<FacilityTabMapper, FacilityTab>
        implements FacilityTabService {
}
