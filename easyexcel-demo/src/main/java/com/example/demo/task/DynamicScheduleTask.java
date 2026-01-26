package com.example.demo.task;

import com.example.demo.config.CronConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;

@Configuration
@EnableScheduling
public class DynamicScheduleTask implements SchedulingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(DynamicScheduleTask.class);

    @Autowired
    private CronConfiguration cronConfiguration;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // Register Task 1
        taskRegistrar.addTriggerTask(
                () -> log.info("Task 1 running at: {}", LocalDateTime.now()),
                getTrigger("task1")
        );

        // Register Task 2
        taskRegistrar.addTriggerTask(
                () -> log.info("Task 2 running at: {}", LocalDateTime.now()),
                getTrigger("task2")
        );
    }

    private Trigger getTrigger(String taskName) {
        return triggerContext -> {
            String cron = cronConfiguration.getCron().get(taskName);
            if (!StringUtils.hasText(cron)) {
                log.warn("Cron expression for {} is empty, retrying in 30 seconds", taskName);
                return new Date(System.currentTimeMillis() + 30000);
            }
            try {
                CronTrigger trigger = new CronTrigger(cron);
                return trigger.nextExecutionTime(triggerContext);
            } catch (IllegalArgumentException e) {
                log.error("Invalid cron expression for {}: {}, retrying in 30 seconds", taskName, cron, e);
                return new Date(System.currentTimeMillis() + 30000);
            }
        };
    }
}
