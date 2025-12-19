package com.ruoyi.system.time.core;

public class SimpleTimeGuard implements TimeGuard {

    private final boolean master;

    public SimpleTimeGuard(boolean master) {
        this.master = master;
    }

    @Override
    public boolean allow() {
        return master;
    }
}
