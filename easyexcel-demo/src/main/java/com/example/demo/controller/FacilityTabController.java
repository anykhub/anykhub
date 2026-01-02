package com.example.demo.controller;

import com.example.demo.entity.FacilityTab;
import com.example.demo.service.FacilityTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设施页签管理 Controller
 */
@RestController
@RequestMapping("/api/facility-tab")
public class FacilityTabController {

    @Autowired
    private FacilityTabService facilityTabService;

    @GetMapping("/list")
    public Map<String, Object> list() {
        List<FacilityTab> list = facilityTabService.list();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", list);
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        FacilityTab tab = facilityTabService.getById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        if (tab != null) {
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", tab);
        } else {
            result.put("code", 404);
            result.put("message", "页签不存在");
        }
        return result;
    }

    @PostMapping
    public Map<String, Object> save(@RequestBody FacilityTab tab) {
        if (tab.getStatus() == null) {
            tab.setStatus("0");
        }
        if (tab.getDelFlag() == null) {
            tab.setDelFlag("0");
        }
        if (tab.getTabOrder() == null) {
            tab.setTabOrder(0);
        }
        tab.setCreateTime(new Date());
        tab.setUpdateTime(new Date());

        boolean success = facilityTabService.save(tab);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "新增成功" : "新增失败");
        result.put("data", tab);
        return result;
    }

    @PutMapping
    public Map<String, Object> update(@RequestBody FacilityTab tab) {
        tab.setUpdateTime(new Date());
        boolean success = facilityTabService.updateById(tab);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        boolean success = facilityTabService.removeById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
}
