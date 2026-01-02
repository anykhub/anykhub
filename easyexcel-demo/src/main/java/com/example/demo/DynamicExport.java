package com.example.demo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.util.ArrayList;
import java.util.List;

public class DynamicExport {

    public static void main(String[] args) {
        // Output file path
        String fileName = "dynamic_multisheet_export.xlsx";

        // Create ExcelWriter
        // Note: When writing to the same file multiple times (multiple sheets), we must use the build() method
        // to get the ExcelWriter object, and finally call finish() to close the stream.
        try (ExcelWriter excelWriter = EasyExcel.write(fileName).build()) {

            // Loop to create multiple sheets
            for (int i = 0; i < 3; i++) {
                // Construct the sheet
                // sheetNo starts from 0
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "Sheet_" + (i + 1))
                        .head(head(i)) // Set dynamic header
                        .build();

                // Write data to the specific sheet
                excelWriter.write(data(i), writeSheet);
            }
        }

        System.out.println("Export completed: " + fileName);
    }

    /**
     * Create dynamic headers.
     * The outer list represents columns, the inner list represents the header hierarchy (rows).
     * For a simple one-row header, the inner list has 1 element.
     */
    private static List<List<String>> head(int sheetIndex) {
        List<List<String>> list = new ArrayList<>();

        // Column 1 Header
        List<String> head0 = new ArrayList<>();
        head0.add("Sheet" + sheetIndex + "_ID");
        list.add(head0);

        // Column 2 Header
        List<String> head1 = new ArrayList<>();
        head1.add("Sheet" + sheetIndex + "_Name");
        list.add(head1);

        // Column 3 Header
        List<String> head2 = new ArrayList<>();
        head2.add("Sheet" + sheetIndex + "_Date");
        list.add(head2);

        return list;
    }

    /**
     * Create dynamic data.
     * The outer list represents rows, the inner list represents cells (columns).
     */
    private static List<List<Object>> data(int sheetIndex) {
        List<List<Object>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<Object> data = new ArrayList<>();
            data.add("ID_" + sheetIndex + "_" + i);
            data.add("Name_" + i);
            data.add("2023-10-" + (10 + i));
            list.add(data);
        }
        return list;
    }
}
