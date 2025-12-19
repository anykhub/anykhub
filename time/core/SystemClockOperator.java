package com.ruoyi.system.time.core;


import java.util.Date;

public interface SystemClockOperator {

    /** 设置系统时间 */
    void set(Date date);

    /** 读取系统时间 */
    Date get();
}
