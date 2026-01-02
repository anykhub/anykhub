package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.MilitaryStandard;
import com.example.demo.service.MilitaryStandardService;
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
 * 军标资源管理 Controller
 */
@RestController
@RequestMapping("/api/military-standard")
public class MilitaryStandardController {

    @Autowired
    private MilitaryStandardService militaryStandardService;

    @GetMapping("/list")
    public Map<String, Object> list() {
        List<MilitaryStandard> list = militaryStandardService.list();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", list);
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        MilitaryStandard ms = militaryStandardService.getById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        if (ms != null) {
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", ms);
        } else {
            result.put("code", 404);
            result.put("message", "军标资源不存在");
        }
        return result;
    }

    @PostMapping
    public Map<String, Object> save(@RequestBody MilitaryStandard ms) {
        if (ms.getStatus() == null) {
            ms.setStatus("0");
        }
        if (ms.getDelFlag() == null) {
            ms.setDelFlag("0");
        }
        ms.setCreateTime(new Date());
        ms.setUpdateTime(new Date());

        boolean success = militaryStandardService.save(ms);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "新增成功" : "新增失败");
        result.put("data", ms);
        return result;
    }

    @PutMapping
    public Map<String, Object> update(@RequestBody MilitaryStandard ms) {
        ms.setUpdateTime(new Date());
        boolean success = militaryStandardService.updateById(ms);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        boolean success = militaryStandardService.removeById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    @GetMapping("/page")
    public Map<String, Object> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        Page<MilitaryStandard> page = new Page<MilitaryStandard>(current, size);
        Page<MilitaryStandard> msPage = militaryStandardService.page(page);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("records", msPage.getRecords());
        data.put("total", msPage.getTotal());
        data.put("current", msPage.getCurrent());
        data.put("size", msPage.getSize());
        data.put("pages", msPage.getPages());

        result.put("data", data);
        return result;
    }
}
