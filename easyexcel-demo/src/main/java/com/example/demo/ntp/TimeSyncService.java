package com.example.demo.ntp;


import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TimeSyncService {

    @Autowired
    private DiscoveryClient discoveryClient;

    // 维护 本地系统时间 与 标准时间 的差值
    private final AtomicLong timeOffset = new AtomicLong(0);

    // 服务端在 Nacos 里的名字
    private static final String SERVER_NAME = "ntp-server";

    @PostConstruct
    public void init() {
        // 启动后同步一次，之后可以安排定时任务每隔几分钟同步一次
        new Thread(() -> {
            while (true) {
                syncTime();
                try { Thread.sleep(60 * 1000); } catch (InterruptedException e) {} // 每分钟同步
            }
        }).start();
    }

    private void syncTime() {
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(2000);

        try {
            // 1. 从 Nacos 发现服务实例
            List<ServiceInstance> instances = discoveryClient.getInstances(SERVER_NAME);
            if (instances.isEmpty()) {
                System.err.println("未找到 NTP 服务提供者: " + SERVER_NAME);
                return;
            }

            // 简单负载均衡：取第一个 (或者随机取)
            ServiceInstance instance = instances.get(0);
            String ip = instance.getHost();
            Map<String, String> metadata = instance.getMetadata();

            // 2. 从 Metadata 中获取 UDP 端口
            if (!metadata.containsKey("ntp-port")) {
                System.err.println("服务端未配置 ntp-port 元数据");
                return;
            }
            int udpPort = Integer.parseInt(metadata.get("ntp-port"));

            // 3. 发起 NTP 请求
            System.out.println("正在向 NTP 服务端同步: " + ip + ":" + udpPort);
            InetAddress hostAddr = InetAddress.getByName(ip);
            TimeInfo info = client.getTime(hostAddr, udpPort);
            info.computeDetails();

            // 4. 更新时间偏差
            Long offset = info.getOffset();
            if (offset != null) {
                timeOffset.set(offset);
                System.out.println("同步成功，本地偏差修正: " + offset + "ms");
            }

        } catch (Exception e) {
            System.err.println("同步失败: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    /**
     * 获取经过 NTP 修正后的当前时间
     */
    public long getCorrectedTime() {
        return System.currentTimeMillis() + timeOffset.get();
    }

}
