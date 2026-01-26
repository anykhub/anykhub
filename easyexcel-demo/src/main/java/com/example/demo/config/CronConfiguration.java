package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class CronConfiguration {

    @Value("${task.cron:0/10 * * * * ?}")
    private String cron;

    public String getCron() {
        return cron;
    }
}
