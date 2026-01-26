package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "task")
public class CronConfiguration {

    private Map<String, String> cron = new HashMap<>();

    public Map<String, String> getCron() {
        return cron;
    }

    public void setCron(Map<String, String> cron) {
        this.cron = cron;
    }
}
