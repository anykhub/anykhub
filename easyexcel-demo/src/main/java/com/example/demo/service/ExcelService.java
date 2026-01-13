package com.example.demo.service;

import com.example.demo.ProjectExportVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelService {

    /**
     * 将复杂的嵌套数据转换为平铺的导出数据
     */
    public List<ProjectExportVO> buildExportData(List<ProjectDto> projectList) {
        List<ProjectExportVO> result = new ArrayList<>();

        for (ProjectDto project : projectList) {
            // 1. 获取子列表，防空处理
            List<MemberDto> members = project.getMembers() != null ? project.getMembers() : Collections.emptyList();
            List<DeviceDto> devices = project.getDevices() != null ? project.getDevices() : Collections.emptyList();

            // 2. 核心：计算最大行数 (Max Rows)
            // 该项目在 Excel 中占用的总行数，取决于哪个子列表更长
            int maxRows = Math.max(members.size(), devices.size());
            // 如果都为空，至少保留一行展示项目本身的信息
            if (maxRows == 0) maxRows = 1;

            // 3. 循环生成每一行
            for (int i = 0; i < maxRows; i++) {
                ProjectExportVO vo = new ProjectExportVO();

                // --- A. 填充主数据 (每行都填，由合并策略处理视觉效果) ---
                vo.setProjectId(project.getId()); // 关键：用于合并策略识别
                vo.setProjectName(project.getName());
                vo.setManager(project.getManager());

                // --- B. 填充成员信息 (如果索引在范围内) ---
                if (i < members.size()) {
                    MemberDto member = members.get(i);
                    vo.setMemberName(member.getName());
                    vo.setMemberRole(member.getRole());
                }

                // --- C. 填充设备信息 (如果索引在范围内) ---
                if (i < devices.size()) {
                    DeviceDto device = devices.get(i);
                    vo.setDeviceName(device.getName());
                    vo.setDeviceCode(device.getCode());
                }

                result.add(vo);
            }
        }
        return result;
    }

    // ============ 模拟的 DTO 类 (仅用于演示) ============
    @Data
    public static class ProjectDto {
        private String id;
        private String name;
        private String manager;
        private List<MemberDto> members;
        private List<DeviceDto> devices;
    }
    @Data
    public static class MemberDto {
        private String name;
        private String role;
        public MemberDto(String name, String role) { this.name = name; this.role = role; }
    }
    @Data
    public static class DeviceDto {
        private String name;
        private String code;
        public DeviceDto(String name, String code) { this.name = name; this.code = code; }
    }
}
