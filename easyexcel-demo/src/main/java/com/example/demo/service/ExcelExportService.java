package com.example.demo.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel Export Service
 * Provides dynamic multi-sheet Excel export functionality
 */
@Service
public class ExcelExportService {

    /**
     * Export dynamic multi-sheet Excel file
     * 
     * @param sheetCount Number of sheets to create
     * @return Excel file as byte array
     */
    public byte[] exportDynamicMultiSheet(int sheetCount) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (ExcelWriter excelWriter = EasyExcel.write(outputStream).build()) {
            // Loop to create multiple sheets
            for (int i = 0; i < sheetCount; i++) {
                // Construct the sheet
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "Sheet_" + (i + 1))
                        .head(createHead(i))
                        .build();

                // Write data to the specific sheet
                excelWriter.write(createData(i), writeSheet);
            }
        }

        return outputStream.toByteArray();
    }

    /**
     * Create dynamic headers
     * The outer list represents columns, the inner list represents the header
     * hierarchy (rows)
     * For a simple one-row header, the inner list has 1 element
     */
    private List<List<String>> createHead(int sheetIndex) {
        List<List<String>> list = new ArrayList<List<String>>();

        // Column 1 Header
        List<String> head0 = new ArrayList<String>();
        head0.add("Sheet" + sheetIndex + "_ID");
        list.add(head0);

        // Column 2 Header
        List<String> head1 = new ArrayList<String>();
        head1.add("Sheet" + sheetIndex + "_Name");
        list.add(head1);

        // Column 3 Header
        List<String> head2 = new ArrayList<String>();
        head2.add("Sheet" + sheetIndex + "_Date");
        list.add(head2);

        return list;
    }

    /**
     * Create dynamic data
     * The outer list represents rows, the inner list represents cells (columns)
     */
    private List<List<Object>> createData(int sheetIndex) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        for (int i = 0; i < 10; i++) {
            List<Object> data = new ArrayList<Object>();
            data.add("ID_" + sheetIndex + "_" + i);
            data.add("Name_" + i);
            data.add("2023-10-" + (10 + i));
            list.add(data);
        }
        return list;
    }
}
