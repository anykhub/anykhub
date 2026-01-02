package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.MilitaryStandard;
import com.example.demo.mapper.MilitaryStandardMapper;
import com.example.demo.service.MilitaryStandardService;
import org.springframework.stereotype.Service;

/**
 * 军标资源 Service 实现类
 */
@Service
public class MilitaryStandardServiceImpl extends ServiceImpl<MilitaryStandardMapper, MilitaryStandard>
        implements MilitaryStandardService {
}
