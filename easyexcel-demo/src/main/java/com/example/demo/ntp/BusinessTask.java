package com.example.demo.ntp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
public class BusinessTask {

    @Autowired
    private TimeSyncService timeSyncService;

    /**
     * 每秒执行
     * 基础版：如果你的服务器负载不高，GC 正常（停顿 < 500ms），只用向下取整完全没问题。
     * 进阶版：如果你想追求完美，或者担心服务器偶尔抽风，请加上 NTP 校准。
     * 完美版：NTP + 向下取整 + 相位缓冲 (避开 .000 秒执行)。
     */
    @Scheduled(cron = "* * * * * *")
    public void report() {
        // 1. 获取校准后的时间
        long now = timeSyncService.getCorrectedTime();

        // ---------------------------------------------------------
        // 【新增】防抖动逻辑：相位偏移保护
        // 如果当前时间距离整秒太近（比如 < 50ms），说明可能就在边界上
        // 我们稍微等一下，让它落入“绝对安全区”
        // ---------------------------------------------------------
        long millisPart = now % 1000;
        if (millisPart < 50) {
            try {
                // 睡 50ms，确保时间从 00.999 或 01.005 变成 01.055+
                // 这样向下取整就绝对稳了
                Thread.sleep(50);
                // 重新获取时间
                now = timeSyncService.getCorrectedTime();
            } catch (InterruptedException e) {}
        }

        // 2. 向下取整 (核心算法)
        long logicTime = (now / 1000) * 1000;

        // 3. 极端滞后检查 (可选，防止 GC 卡顿超过 1秒的情况)
        // 如果发现当前秒数和上一次上报的秒数差了 2秒以上，说明中间漏发了，或者跳秒了
        // 这里根据业务决定是否补发，通常直接上报即可

        Date reportTime = new Date(logicTime);
        // 上报...
    }
}
