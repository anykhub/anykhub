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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, String> cronMap = new HashMap<>();
        cronMap.put("task1", "0/5 * * * * ?");
        cronMap.put("task2", "0/10 * * * * ?");
        when(cronConfiguration.getCron()).thenReturn(cronMap);

        // Execute
        dynamicScheduleTask.configureTasks(taskRegistrar);

        // Verify
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Trigger> triggerCaptor = ArgumentCaptor.forClass(Trigger.class);

        // We expect 2 calls
        verify(taskRegistrar, times(2)).addTriggerTask(runnableCaptor.capture(), triggerCaptor.capture());

        List<Trigger> triggers = triggerCaptor.getAllValues();
        assertNotNull(triggers);

        TriggerContext context = new TriggerContext() {
            @Override
            public Date lastScheduledExecutionTime() { return null; }
            @Override
            public Date lastActualExecutionTime() { return null; }
            @Override
            public Date lastCompletionTime() { return null; }
        };

        // Check trigger 1
        Date nextExecution1 = triggers.get(0).nextExecutionTime(context);
        assertNotNull(nextExecution1);

        // Check trigger 2
        Date nextExecution2 = triggers.get(1).nextExecutionTime(context);
        assertNotNull(nextExecution2);
    }
}
