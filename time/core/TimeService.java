package com.ruoyi.system.time.core;


import java.util.Date;

public final class TimeService {

    private final SystemClockOperator operator;
    private final TimeGuard guard;

    public TimeService(TimeGuard guard) {
        this.guard = guard;

        if (OS.isWindows()) {
            this.operator = new WindowsClockOperator();
        } else if (OS.isLinux()) {
            this.operator = new LinuxClockOperator();
        } else {
            throw new UnsupportedOperationException("Unsupported OS");
        }
    }

    /**
     * 安全修改系统时间
     */
    public void setSystemTime(Date target) {
        if (!guard.allow()) {
            throw new IllegalStateException("Current node is not allowed to modify system time");
        }

        Date before = operator.get();

        try {
            operator.set(target);

            // 回读校验（允许 1s 误差）
            Date after = operator.get();
            if (Math.abs(after.getTime() - target.getTime()) > 1000) {
                throw new IllegalStateException("Time verification failed");
            }

        } catch (Exception e) {
            // 回滚
            operator.set(before);
            throw e;
        }
    }
}
