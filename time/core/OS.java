package com.ruoyi.system.time.core;

public class OS {
    
    private static final String NAME = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return NAME.contains("win");
    }

    public static boolean isLinux() {
        return NAME.contains("linux");
    }
}
