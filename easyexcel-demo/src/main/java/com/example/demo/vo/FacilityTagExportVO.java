package com.example.demo.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设施-标签关联导出 VO
 */
@Data
public class FacilityTagExportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("设施编码")
    @ColumnWidth(15)
    private String facilityCode;

    @ExcelProperty("设施名称")
    @ColumnWidth(20)
    private String facilityName;

    @ExcelProperty("标签编码")
    @ColumnWidth(15)
    private String tagCode;

    @ExcelProperty("标签名称")
    @ColumnWidth(15)
    private String tagName;

    @ExcelProperty("标签颜色")
    @ColumnWidth(12)
    private String tagColor;

    @ExcelProperty("标签类型")
    @ColumnWidth(12)
    private String tagType;

    @ExcelProperty("关联时间")
    @ColumnWidth(20)
    private Date createTime;
}
