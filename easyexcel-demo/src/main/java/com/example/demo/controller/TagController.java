package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Tag;
import com.example.demo.service.TagService;
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
 * 标签管理 Controller
 */
@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public Map<String, Object> list() {
        List<Tag> list = tagService.list();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", list);
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        Tag tag = tagService.getById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        if (tag != null) {
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", tag);
        } else {
            result.put("code", 404);
            result.put("message", "标签不存在");
        }
        return result;
    }

    @PostMapping
    public Map<String, Object> save(@RequestBody Tag tag) {
        if (tag.getStatus() == null) {
            tag.setStatus("0");
        }
        if (tag.getDelFlag() == null) {
            tag.setDelFlag("0");
        }
        if (tag.getTagOrder() == null) {
            tag.setTagOrder(0);
        }
        tag.setCreateTime(new Date());
        tag.setUpdateTime(new Date());

        boolean success = tagService.save(tag);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "新增成功" : "新增失败");
        result.put("data", tag);
        return result;
    }

    @PutMapping
    public Map<String, Object> update(@RequestBody Tag tag) {
        tag.setUpdateTime(new Date());
        boolean success = tagService.updateById(tag);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        boolean success = tagService.removeById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    @GetMapping("/page")
    public Map<String, Object> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        Page<Tag> page = new Page<Tag>(current, size);
        Page<Tag> tagPage = tagService.page(page);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("records", tagPage.getRecords());
        data.put("total", tagPage.getTotal());
        data.put("current", tagPage.getCurrent());
        data.put("size", tagPage.getSize());
        data.put("pages", tagPage.getPages());

        result.put("data", data);
        return result;
    }
}
