package com.example.demo.task;

import com.example.demo.config.CronConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class DynamicScheduleTask implements SchedulingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(DynamicScheduleTask.class);

    @Autowired
    private CronConfiguration cronConfiguration;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                () -> {
                    // Task logic
                    log.info("Dynamic task running at: {}", LocalDateTime.now());
                },
                triggerContext -> {
                    String cron = cronConfiguration.getCron();
                    if (!StringUtils.hasText(cron)) {
                        log.warn("Cron expression is empty, retrying in 30 seconds");
                        return new java.util.Date(System.currentTimeMillis() + 30000);
                    }
                    try {
                        CronTrigger trigger = new CronTrigger(cron);
                        return trigger.nextExecutionTime(triggerContext);
                    } catch (IllegalArgumentException e) {
                        log.error("Invalid cron expression: {}, retrying in 30 seconds", cron, e);
                        return new java.util.Date(System.currentTimeMillis() + 30000);
                    }
                }
        );
    }
}
