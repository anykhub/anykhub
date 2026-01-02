package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Tag;
import com.example.demo.mapper.TagMapper;
import com.example.demo.service.TagService;
import org.springframework.stereotype.Service;

/**
 * 标签 Service 实现类
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}
