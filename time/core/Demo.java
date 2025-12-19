package com.ruoyi.system.time.core;

import java.util.Date;

public class Demo {

    public static void main(String[] args) {
        TimeGuard guard = new SimpleTimeGuard(true); // 你选中的节点
        TimeService timeService = new TimeService(guard);
        Date target = new Date(System.currentTimeMillis() + 100_000);
        timeService.setSystemTime(target);
        System.out.println("系统时间已修改");
    }
}
