package com.ruoyi.system.time.core;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

public class WindowsClockOperator implements SystemClockOperator{
    @Override
    public void set(Date date) {
        ZonedDateTime utc = date.toInstant().atZone(ZoneOffset.UTC);

        WinBase.SYSTEMTIME st = new WinBase.SYSTEMTIME();
        st.wYear  = (short) utc.getYear();
        st.wMonth = (short) utc.getMonthValue();
        st.wDay   = (short) utc.getDayOfMonth();
        st.wHour  = (short) utc.getHour();
        st.wMinute = (short) utc.getMinute();
        st.wSecond = (short) utc.getSecond();
        st.wMilliseconds = (short) (utc.getNano() / 1_000_000);

        if (!Kernel32.INSTANCE.SetSystemTime(st)) {
            throw new IllegalStateException("SetSystemTime failed");
        }
    }

    @Override
    public Date get() {
        WinBase.SYSTEMTIME st = new WinBase.SYSTEMTIME();
        Kernel32.INSTANCE.GetSystemTime(st);

        return Date.from(
                java.time.ZonedDateTime.of(
                        st.wYear, st.wMonth, st.wDay,
                        st.wHour, st.wMinute, st.wSecond,
                        st.wMilliseconds * 1_000_000,
                        ZoneOffset.UTC
                ).toInstant()
        );
    }
}
