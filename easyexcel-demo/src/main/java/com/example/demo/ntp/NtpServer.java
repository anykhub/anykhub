package com.example.demo.ntp;

import org.apache.commons.net.ntp.NtpV3Impl;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Component
public class NtpServer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(NtpServer.class);

    @Value("${ntp.port:12300}")
    private int port;

    private DatagramSocket socket;
    private volatile boolean running;

    @Override
    public void run(String... args) throws Exception {
        socket = new DatagramSocket(port);
        running = true;

        Thread serverThread = new Thread(this::listen, "NtpServer-Thread");
        serverThread.setDaemon(true); // Daemon thread so it doesn't prevent JVM shutdown
        serverThread.start();

        logger.info("NTP Server started on port {}", port);
    }

    private void listen() {
        byte[] buffer = new byte[48];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (running) {
            try {
                socket.receive(packet);
                handlePacket(packet);
            } catch (IOException e) {
                if (running) {
                    logger.error("Error receiving NTP packet", e);
                }
            }
        }
    }

    private void handlePacket(DatagramPacket requestPacket) {
        try {
            long receiveTime = System.currentTimeMillis();

            NtpV3Impl message = new NtpV3Impl();
            message.setDatagramPacket(requestPacket);

            NtpV3Impl response = new NtpV3Impl();

            // Set mode to SERVER
            response.setMode(NtpV3Packet.MODE_SERVER);
            response.setPoll(message.getPoll());
            response.setVersion(NtpV3Packet.VERSION_3);
            response.setStratum(1);

            // Timestamps
            TimeStamp receiveTimeStamp = TimeStamp.getNtpTime(receiveTime);

            response.setOriginateTimeStamp(message.getTransmitTimeStamp());
            response.setReceiveTimeStamp(receiveTimeStamp);
            response.setReferenceTime(receiveTimeStamp); // Simplified
            response.setTransmitTime(TimeStamp.getCurrentTime());

            DatagramPacket responsePacket = response.getDatagramPacket();
            responsePacket.setAddress(requestPacket.getAddress());
            responsePacket.setPort(requestPacket.getPort());

            socket.send(responsePacket);

        } catch (Exception e) {
            logger.error("Error handling NTP packet", e);
        }
    }

    @PreDestroy
    public void stop() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        logger.info("NTP Server stopped");
    }
}
