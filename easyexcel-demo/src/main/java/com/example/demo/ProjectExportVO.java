package com.example.demo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.Data;

@ContentStyle(
        horizontalAlignment = HorizontalAlignmentEnum.CENTER,
        verticalAlignment = VerticalAlignmentEnum.CENTER
)
// 2. (可选) 设置表头样式：水平居中
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@Data
public class ProjectExportVO {

    // ================= 主数据 (需要合并) =================
    // 使用 ExcelIgnore 标记不需要导出的辅助字段，但在合并策略中需要用到
    @ExcelIgnore
    private String projectId;

    @ExcelProperty("项目名称")
    @ColumnWidth(25)
    private String projectName;

    @ExcelProperty("项目负责人")
    @ColumnWidth(15)
    private String manager;

    // ================= 子数据1：成员信息 =================
    @ExcelProperty({"成员","成员姓名"})
    @ColumnWidth(15)
    private String memberName;

    @ExcelProperty({"成员","成员岗位"})
    @ColumnWidth(15)
    private String memberRole;

    // ================= 子数据2：设备信息 =================
    @ExcelProperty({"设备","设备名称"})
    @ColumnWidth(20)
    private String deviceName;

    @ExcelProperty({"设备","设备编号"})
    @ColumnWidth(20)
    private String deviceCode;
}
