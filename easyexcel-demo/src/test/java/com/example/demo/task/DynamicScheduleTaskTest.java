package com.example.demo.task;

import com.example.demo.config.CronConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DynamicScheduleTaskTest {

    @Mock
    private CronConfiguration cronConfiguration;

    @InjectMocks
    private DynamicScheduleTask dynamicScheduleTask;

    @Mock
    private ScheduledTaskRegistrar taskRegistrar;

    @Test
    public void testConfigureTasks() {
        // Setup
        when(cronConfiguration.getCron()).thenReturn("0/5 * * * * ?");

        // Execute
        dynamicScheduleTask.configureTasks(taskRegistrar);

        // Verify
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Trigger> triggerCaptor = ArgumentCaptor.forClass(Trigger.class);
        verify(taskRegistrar).addTriggerTask(runnableCaptor.capture(), triggerCaptor.capture());

        // Test the trigger
        Trigger trigger = triggerCaptor.getValue();
        TriggerContext context = new TriggerContext() {
            @Override
            public Date lastScheduledExecutionTime() {
                return null;
            }

            @Override
            public Date lastActualExecutionTime() {
                return null;
            }

            @Override
            public Date lastCompletionTime() {
                return null;
            }
        };

        // First call
        Date nextExecution = trigger.nextExecutionTime(context);
        assertNotNull(nextExecution);

        // Simulate dynamic change
        when(cronConfiguration.getCron()).thenReturn("0/10 * * * * ?");
        Date nextExecution2 = trigger.nextExecutionTime(context);
        assertNotNull(nextExecution2);
    }
}
