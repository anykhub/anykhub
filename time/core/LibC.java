package com.ruoyi.system.time.core;

import com.sun.jna.*;
import java.util.*;

public interface LibC extends Library {

    LibC INSTANCE = Native.load("c", LibC.class);

    int CLOCK_REALTIME = 0;

    int clock_settime(int clk_id, Timespec tp);
    int clock_gettime(int clk_id, Timespec tp);

    class Timespec extends Structure {
        public long tv_sec;
        public long tv_nsec;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("tv_sec", "tv_nsec");
        }
    }
}
