package com.example.demo;

import com.alibaba.excel.EasyExcel;
import com.example.demo.handler.FlexibleMergeStrategy;
import com.example.demo.service.ExcelService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExportTest {
    @Test
    public void testExport() {
        // 1. 模拟数据库查询出的嵌套数据
        List<ExcelService.ProjectDto> dbData = mockDbData();

        // 2. 数据转换 (核心逻辑调用)
        ExcelService service = new ExcelService();
        List<ProjectExportVO> exportData = service.buildExportData(dbData);

        // 3. 定义文件名
        String fileName = "D:\\复杂业务导出_" + System.currentTimeMillis() + ".xlsx";

        // 4. 定义哪些列需要合并 (列索引从0开始)
        // ProjectExportVO 中: ProjectName(0), Manager(1)
        // 注意：projectId 加了 @ExcelIgnore，所以它不占用列索引，显示的列是 0 和 1
        int[] mergeColIndices = {0, 1};

        // 5. 执行导出
        EasyExcel.write(fileName, ProjectExportVO.class)
                // 注册自定义合并策略：依据 "projectId" 字段，合并第 0,1 列
                .registerWriteHandler(new FlexibleMergeStrategy(exportData, "projectId", mergeColIndices))
                .sheet("项目数据")
                .doWrite(exportData);

        System.out.println("导出成功，文件路径：" + fileName);
    }

    // 模拟数据构造
    private List<ExcelService.ProjectDto> mockDbData() {
        List<ExcelService.ProjectDto> list = new ArrayList<>();

        // === 项目A：2个成员，3个设备 (行数=3) ===
        ExcelService.ProjectDto p1 = new ExcelService.ProjectDto();
        p1.setId("P1001");
        p1.setName("智慧城市改造");
        p1.setManager("张三");
        p1.setMembers(Arrays.asList(
                new ExcelService.MemberDto("李四", "架构师"),
                new ExcelService.MemberDto("王五", "开发")
        ));
        p1.setDevices(Arrays.asList(
                new ExcelService.DeviceDto("摄像头", "C001"),
                new ExcelService.DeviceDto("传感器", "S001"),
                new ExcelService.DeviceDto("网关", "G001")
        ));
        list.add(p1);

        // === 项目B：1个成员，0个设备 (行数=1) ===
        ExcelService.ProjectDto p2 = new ExcelService.ProjectDto();
        p2.setId("P1002");
        p2.setName("内部OA升级");
        p2.setManager("赵六");
        p2.setMembers(Arrays.asList(
                new ExcelService.MemberDto("孙七", "测试")
        ));
        // devices 为 null 或空
        list.add(p2);

        return list;
    }
}
