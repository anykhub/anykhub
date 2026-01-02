package com.example.demo.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设施导出 VO
 */
@Data
public class FacilityExportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("设施ID")
    @ColumnWidth(10)
    private Long facilityId;

    @ExcelProperty("设施编码")
    @ColumnWidth(15)
    private String facilityCode;

    @ExcelProperty("设施名称")
    @ColumnWidth(20)
    private String facilityName;

    @ExcelProperty("父设施ID")
    @ColumnWidth(10)
    private Long parentFacilityId;

    @ExcelProperty("父设施名称")
    @ColumnWidth(20)
    private String parentFacilityName;

    @ExcelProperty("页签ID")
    @ColumnWidth(10)
    private Long tabId;

    @ExcelProperty("页签名称")
    @ColumnWidth(15)
    private String tabName;

    @ExcelProperty("国家代码")
    @ColumnWidth(10)
    private String countryCode;

    @ExcelProperty("国家名称")
    @ColumnWidth(12)
    private String countryName;

    @ExcelProperty("设施类型")
    @ColumnWidth(10)
    private String facilityType;

    @ExcelProperty("位置")
    @ColumnWidth(25)
    private String location;

    @ExcelProperty("纬度")
    @ColumnWidth(12)
    private BigDecimal latitude;

    @ExcelProperty("经度")
    @ColumnWidth(12)
    private BigDecimal longitude;

    @ExcelProperty("面积")
    @ColumnWidth(10)
    private BigDecimal area;

    @ExcelProperty("容量")
    @ColumnWidth(10)
    private Integer capacity;

    @ExcelProperty("描述")
    @ColumnWidth(30)
    private String description;

    @ExcelProperty("状态")
    @ColumnWidth(8)
    private String status;

    @ExcelProperty("创建人")
    @ColumnWidth(12)
    private String createBy;

    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private Date createTime;

    @ExcelProperty("备注")
    @ColumnWidth(30)
    private String remark;
}
