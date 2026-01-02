package com.example.demo.controller;

import com.example.demo.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Excel Export Controller
 * Provides REST API endpoints for Excel file export
 */
@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private ExcelExportService excelExportService;

    /**
     * Export dynamic multi-sheet Excel file
     * 
     * @param sheetCount Number of sheets to create (default: 3)
     * @return Excel file download
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam(value = "sheetCount", defaultValue = "3") int sheetCount) {

        // Generate Excel file
        byte[] excelBytes = excelExportService.exportDynamicMultiSheet(sheetCount);

        // Generate filename with timestamp
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String filename = "dynamic_multisheet_export_" + timestamp + ".xlsx";

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        try {
            // URL encode filename to support Chinese characters
            String encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
            headers.setContentDispositionFormData("attachment", encodedFilename);
        } catch (UnsupportedEncodingException e) {
            headers.setContentDispositionFormData("attachment", filename);
        }

        return new ResponseEntity<byte[]>(excelBytes, headers, HttpStatus.OK);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public String health() {
        return "Excel Export Service is running!";
    }
}
