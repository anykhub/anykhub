package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Facility;
import com.example.demo.entity.MilitaryStandard;
import com.example.demo.entity.Tag;
import com.example.demo.service.FacilityService;
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
 * 设施管理 Controller
 */
@RestController
@RequestMapping("/api/facility")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @GetMapping("/list")
    public Map<String, Object> list() {
        List<Facility> list = facilityService.list();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", list);
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        Facility facility = facilityService.getById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        if (facility != null) {
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", facility);
        } else {
            result.put("code", 404);
            result.put("message", "设施不存在");
        }
        return result;
    }

    @PostMapping
    public Map<String, Object> save(@RequestBody Facility facility) {
        if (facility.getStatus() == null) {
            facility.setStatus("0");
        }
        if (facility.getDelFlag() == null) {
            facility.setDelFlag("0");
        }
        facility.setCreateTime(new Date());
        facility.setUpdateTime(new Date());

        boolean success = facilityService.save(facility);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "新增成功" : "新增失败");
        result.put("data", facility);
        return result;
    }

    @PutMapping
    public Map<String, Object> update(@RequestBody Facility facility) {
        facility.setUpdateTime(new Date());
        boolean success = facilityService.updateById(facility);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        boolean success = facilityService.removeById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    @GetMapping("/page")
    public Map<String, Object> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        Page<Facility> page = new Page<Facility>(current, size);
        Page<Facility> facilityPage = facilityService.page(page);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("records", facilityPage.getRecords());
        data.put("total", facilityPage.getTotal());
        data.put("current", facilityPage.getCurrent());
        data.put("size", facilityPage.getSize());
        data.put("pages", facilityPage.getPages());

        result.put("data", data);
        return result;
    }

    // ==================== 军标资源关联管理 ====================

    @GetMapping("/{id}/military-standards")
    public Map<String, Object> getMilitaryStandards(@PathVariable Long id) {
        List<MilitaryStandard> list = facilityService.getMilitaryStandardsByFacilityId(id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", list);
        return result;
    }

    @PostMapping("/{id}/military-standards")
    public Map<String, Object> bindMilitaryStandards(
            @PathVariable Long id,
            @RequestBody List<Long> msIds) {
        boolean success = facilityService.bindMilitaryStandards(id, msIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "绑定成功" : "绑定失败");
        return result;
    }

    @DeleteMapping("/{id}/military-standards")
    public Map<String, Object> unbindMilitaryStandards(
            @PathVariable Long id,
            @RequestBody List<Long> msIds) {
        boolean success = facilityService.unbindMilitaryStandards(id, msIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "解绑成功" : "解绑失败");
        return result;
    }

    // ==================== 标签关联管理 ====================

    @GetMapping("/{id}/tags")
    public Map<String, Object> getTags(@PathVariable Long id) {
        List<Tag> list = facilityService.getTagsByFacilityId(id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", list);
        return result;
    }

    @PostMapping("/{id}/tags")
    public Map<String, Object> bindTags(
            @PathVariable Long id,
            @RequestBody List<Long> tagIds) {
        boolean success = facilityService.bindTags(id, tagIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "绑定成功" : "绑定失败");
        return result;
    }

    @DeleteMapping("/{id}/tags")
    public Map<String, Object> unbindTags(
            @PathVariable Long id,
            @RequestBody List<Long> tagIds) {
        boolean success = facilityService.unbindTags(id, tagIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "解绑成功" : "解绑失败");
        return result;
    }
}
