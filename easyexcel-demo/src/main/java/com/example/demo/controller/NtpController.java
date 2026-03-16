package com.example.demo.controller;

import com.example.demo.ntp.NtpClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ntp")
public class NtpController {

    @Autowired
    private NtpClientService ntpClientService;

    @Value("${ntp.port:12300}")
    private int ntpPort;

    @GetMapping("/sync")
    public Map<String, Object> syncTime(@RequestParam(defaultValue = "localhost") String host) {
        return ntpClientService.getNtpTime(host, ntpPort);
    }
}
