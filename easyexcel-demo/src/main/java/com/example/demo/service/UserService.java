package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.User;

/**
 * 用户 Service 接口
 */
public interface UserService extends IService<User> {
    // 继承 IService 后，自动拥有常用业务方法
    // 可以在这里添加自定义业务方法
}
