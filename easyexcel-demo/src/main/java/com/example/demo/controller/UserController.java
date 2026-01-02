package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理 Controller
 * 提供用户 CRUD 操作的 REST API
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询所有用户
     */
    @GetMapping("/list")
    public Map<String, Object> list() {
        List<User> users = userService.list();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", users);
        return result;
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        if (user != null) {
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", user);
        } else {
            result.put("code", 404);
            result.put("message", "用户不存在");
        }
        return result;
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Map<String, Object> save(@RequestBody User user) {
        // 设置默认值
        if (user.getUserType() == null) {
            user.setUserType("00");
        }
        if (user.getStatus() == null) {
            user.setStatus("0");
        }
        if (user.getDelFlag() == null) {
            user.setDelFlag("0");
        }
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        boolean success = userService.save(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "新增成功" : "新增失败");
        result.put("data", user);
        return result;
    }

    /**
     * 更新用户
     */
    @PutMapping
    public Map<String, Object> update(@RequestBody User user) {
        user.setUpdateTime(new Date());
        boolean success = userService.updateById(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    /**
     * 删除用户（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        boolean success = userService.removeById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    /**
     * 分页查询用户
     * 
     * @param current 当前页码
     * @param size    每页大小
     */
    @GetMapping("/page")
    public Map<String, Object> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        Page<User> page = new Page<User>(current, size);
        Page<User> userPage = userService.page(page);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("records", userPage.getRecords());
        data.put("total", userPage.getTotal());
        data.put("current", userPage.getCurrent());
        data.put("size", userPage.getSize());
        data.put("pages", userPage.getPages());

        result.put("data", data);
        return result;
    }

    /**
     * 条件查询用户（示例：根据用户名模糊查询）
     */
    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("USER_NAME", userName);
        List<User> users = userService.list(queryWrapper);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", users);
        return result;
    }
}
