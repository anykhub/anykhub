package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Facility;
import com.example.demo.entity.MilitaryStandard;
import com.example.demo.entity.Tag;
import com.example.demo.service.FacilityService;
import com.example.demo.vo.CountryFacilityTree;
import com.example.demo.vo.FacilityTreeNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设施管理 Controller
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "设施管理", description = "设施管理相关接口，包括CRUD、关联管理、树形查询、数据导出等功能")
@RestController
@RequestMapping("/api/facility")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @Operation(summary = "查询所有设施", description = "获取所有未删除的设施列表")
    @GetMapping("/list")
    public Map<String, Object> list() {
        List<Facility> list = facilityService.list();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", list);
        return result;
    }

    @Operation(summary = "根据ID查询设施", description = "根据设施ID获取设施详细信息")
    @GetMapping("/{id}")
    public Map<String, Object> getById(
            @Parameter(description = "设施ID", required = true, example = "1") @PathVariable Long id) {
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

    @Operation(summary = "新增设施", description = "创建一个新的设施")
    @PostMapping
    public Map<String, Object> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "设施信息", required = true) @RequestBody Facility facility) {
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

    @Operation(summary = "更新设施", description = "更新设施信息")
    @PutMapping
    public Map<String, Object> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "设施信息", required = true) @RequestBody Facility facility) {
        facility.setUpdateTime(new Date());
        boolean success = facilityService.updateById(facility);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    @Operation(summary = "删除设施", description = "逻辑删除指定设施")
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(
            @Parameter(description = "设施ID", required = true, example = "1") @PathVariable Long id) {
        boolean success = facilityService.removeById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    @Operation(summary = "分页查询设施", description = "分页获取设施列表")
    @GetMapping("/page")
    public Map<String, Object> page(
            @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Long size) {
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

    @Operation(summary = "查询设施关联的军标资源", description = "获取指定设施关联的所有军标资源")
    @GetMapping("/{id}/military-standards")
    public Map<String, Object> getMilitaryStandards(
            @Parameter(description = "设施ID", required = true, example = "1") @PathVariable Long id) {
        List<MilitaryStandard> list = facilityService.getMilitaryStandardsByFacilityId(id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", list);
        return result;
    }

    @Operation(summary = "绑定军标资源", description = "为设施绑定一个或多个军标资源")
    @PostMapping("/{id}/military-standards")
    public Map<String, Object> bindMilitaryStandards(
            @Parameter(description = "设施ID", required = true, example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "军标资源ID列表", required = true) @RequestBody List<Long> msIds) {
        boolean success = facilityService.bindMilitaryStandards(id, msIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "绑定成功" : "绑定失败");
        return result;
    }

    @Operation(summary = "解绑军标资源", description = "解除设施与军标资源的关联")
    @DeleteMapping("/{id}/military-standards")
    public Map<String, Object> unbindMilitaryStandards(
            @Parameter(description = "设施ID", required = true, example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "军标资源ID列表", required = true) @RequestBody List<Long> msIds) {
        boolean success = facilityService.unbindMilitaryStandards(id, msIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "解绑成功" : "解绑失败");
        return result;
    }

    // ==================== 标签关联管理 ====================

    @Operation(summary = "查询设施关联的标签", description = "获取指定设施关联的所有标签")
    @GetMapping("/{id}/tags")
    public Map<String, Object> getTags(
            @Parameter(description = "设施ID", required = true, example = "1") @PathVariable Long id) {
        List<Tag> list = facilityService.getTagsByFacilityId(id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", list);
        return result;
    }

    @Operation(summary = "绑定标签", description = "为设施绑定一个或多个标签")
    @PostMapping("/{id}/tags")
    public Map<String, Object> bindTags(
            @Parameter(description = "设施ID", required = true, example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "标签ID列表", required = true) @RequestBody List<Long> tagIds) {
        boolean success = facilityService.bindTags(id, tagIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "绑定成功" : "绑定失败");
        return result;
    }

    @Operation(summary = "解绑标签", description = "解除设施与标签的关联")
    @DeleteMapping("/{id}/tags")
    public Map<String, Object> unbindTags(
            @Parameter(description = "设施ID", required = true, example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "标签ID列表", required = true) @RequestBody List<Long> tagIds) {
        boolean success = facilityService.unbindTags(id, tagIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "解绑成功" : "解绑失败");
        return result;
    }

    // ==================== 设施树查询 ====================

    /**
     * 按国家查询设施树
     * 返回按国家分组的树形结构，包含设施层级、军标资源、标签信息
     */
    @Operation(summary = "按国家查询设施树", description = "返回按国家分组的树形结构，包含设施层级、军标资源、标签信息")
    @GetMapping("/tree/by-country")
    public Map<String, Object> getFacilityTreeByCountry() {
        List<CountryFacilityTree> tree = facilityService.getFacilityTreeByCountry();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", tree);
        return result;
    }

    /**
     * 查询设施树（从指定父节点开始）
     */
    @Operation(summary = "查询设施树", description = "从指定父节点开始查询设施树，为空则查询所有一级设施")
    @GetMapping("/tree")
    public Map<String, Object> getFacilityTree(
            @Parameter(description = "父设施ID，为空则查询所有一级设施", example = "1") @RequestParam(required = false) Long parentId) {
        List<FacilityTreeNode> tree = facilityService.buildFacilityTree(parentId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", tree);
        return result;
    }

    // ==================== 数据导出 ====================

    /**
     * 导出设施数据（多Sheet Excel）
     */
    @Operation(summary = "导出设施数据", description = "导出设施数据到Excel文件，包含设施信息、军标关联、标签关联三个Sheet")
    @GetMapping("/export")
    public void exportFacilityData(
            @Parameter(description = "国家代码，用于筛选导出指定国家的设施", example = "CN") @RequestParam(required = false) String countryCode,
            javax.servlet.http.HttpServletResponse response) throws java.io.IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 生成文件名
        String fileName = "设施数据备份_" + java.time.LocalDate.now();
        if (countryCode != null && !countryCode.trim().isEmpty()) {
            fileName += "_" + countryCode;
        }
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        facilityService.exportFacilityData(response.getOutputStream(), countryCode);
    }

    /**
     * 导出指定设施数据（多Sheet Excel）
     */
    @Operation(summary = "导出指定设施数据", description = "导出指定设施及其子设施的数据到Excel文件")
    @GetMapping("/export/{facilityId}")
    public void exportFacilityDataById(
            @Parameter(description = "设施ID", required = true, example = "1") @PathVariable Long facilityId,
            @Parameter(description = "是否包含子设施", example = "true") @RequestParam(required = false, defaultValue = "true") Boolean includeChildren,
            javax.servlet.http.HttpServletResponse response) throws java.io.IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 生成文件名
        Facility facility = facilityService.getById(facilityId);
        String fileName = "设施数据_" + (facility != null ? facility.getFacilityCode() : facilityId) + "_"
                + java.time.LocalDate.now();
        if (includeChildren) {
            fileName += "_含子设施";
        }
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        facilityService.exportFacilityDataById(response.getOutputStream(), facilityId, includeChildren);
    }
}
