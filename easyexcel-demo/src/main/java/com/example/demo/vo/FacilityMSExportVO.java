package com.example.demo.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设施-军标资源关联导出 VO
 */
@Data
public class FacilityMSExportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("设施编码")
    @ColumnWidth(15)
    private String facilityCode;

    @ExcelProperty("设施名称")
    @ColumnWidth(20)
    private String facilityName;

    @ExcelProperty("军标编码")
    @ColumnWidth(15)
    private String msCode;

    @ExcelProperty("军标名称")
    @ColumnWidth(30)
    private String msName;

    @ExcelProperty("军标分类")
    @ColumnWidth(15)
    private String msCategory;

    @ExcelProperty("关联时间")
    @ColumnWidth(20)
    private Date createTime;
}
