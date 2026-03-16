package com.example.demo.ntp;


import org.apache.commons.net.ntp.NtpV3Impl;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeStamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Component
public class NtpUdpServer implements CommandLineRunner {

    /*# 自定义配置
    custom:
    ntp:
    udp-port: 12300*/

    @Value("${custom.ntp.udp-port}")
    private int udpPort;

    @Override
    public void run(String... args) throws Exception {
        // 在新线程启动 UDP 监听，避免阻塞主线程
        new Thread(this::startServer, "UDP-NTP-Server").start();
    }

    private void startServer() {
        try (DatagramSocket socket = new DatagramSocket(udpPort)) {
            System.out.println(">>> NTP UDP 服务端已启动，监听端口: " + udpPort);

            byte[] buffer = new byte[48]; // NTP 报文固定 48 字节
            while (true) {
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(requestPacket);

                // 1. 记录接收时间 T2
                long rcvTime = System.currentTimeMillis();
                TimeStamp receiveTimeStamp = TimeStamp.getNtpTime(rcvTime);

                // 2. 解析请求
                NtpV3Packet request = new NtpV3Impl();
                request.setDatagramPacket(requestPacket);

                // 3. 构建响应
                NtpV3Impl response = new NtpV3Impl();
                response.setMode(NtpV3Packet.MODE_SERVER);
                response.setVersion(NtpV3Packet.VERSION_3);
                response.setStratum(1);
                response.setPoll(request.getPoll());
                response.setPrecision(-6);

                // 设置 T1 (客户端发送时间)
                response.setOriginateTimeStamp(request.getTransmitTimeStamp());
                // 设置 T2 (服务端接收时间)
                response.setReceiveTimeStamp(receiveTimeStamp);
                // 设置 T3 (服务端发送时间)
                response.setTransmitTime(TimeStamp.getCurrentTime());

                // 4. 发送回包
                DatagramPacket responsePacket = response.getDatagramPacket();
                responsePacket.setAddress(requestPacket.getAddress());
                responsePacket.setPort(requestPacket.getPort());
                socket.send(responsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
