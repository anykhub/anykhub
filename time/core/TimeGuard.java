package com.ruoyi.system.time.core;

public interface TimeGuard {

    /** 是否允许当前节点修改系统时间 */
    boolean allow();
}
