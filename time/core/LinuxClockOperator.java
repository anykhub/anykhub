package com.ruoyi.system.time.core;

import com.sun.jna.Native;

import java.util.Date;

public class LinuxClockOperator implements SystemClockOperator {

    @Override
    public void set(Date date) {
        long ms = date.getTime();

        LibC.Timespec ts = new LibC.Timespec();
        ts.tv_sec  = ms / 1000;
        ts.tv_nsec = (ms % 1000) * 1_000_000;

        int ret = LibC.INSTANCE.clock_settime(LibC.CLOCK_REALTIME, ts);
        if (ret != 0) {
            throw new IllegalStateException(
                "clock_settime failed, errno=" + Native.getLastError()
            );
        }
    }

    @Override
    public Date get() {
        LibC.Timespec ts = new LibC.Timespec();
        LibC.INSTANCE.clock_gettime(LibC.CLOCK_REALTIME, ts);

        return new Date(ts.tv_sec * 1000 + ts.tv_nsec / 1_000_000);
    }
}
