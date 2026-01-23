package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ntp.NtpUtils;
import org.apache.commons.net.ntp.NtpV3Impl;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeStamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Slf4j
@Service
public class NtpServerService {

    @Value("${ntp.port:123}")
    private int port;

    private DatagramSocket socket;
    private Thread listenerThread;
    private volatile boolean running = false;

    @PostConstruct
    public void start() {
        try {
            // If we cannot bind to the port (e.g. 123 requires root), log error but don't crash app
            socket = new DatagramSocket(port);
            running = true;
            listenerThread = new Thread(this::listen, "NtpServer-Thread");
            listenerThread.start();
            log.info("NTP Server started on port {}", port);
        } catch (Exception e) {
            log.error("Failed to start NTP Server on port {}: {}", port, e.getMessage());
        }
    }

    private void listen() {
        byte[] buffer = new byte[48]; // NTP packet size is 48 bytes
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (running && !socket.isClosed()) {
            try {
                packet.setLength(buffer.length); // Reset length before receive
                socket.receive(packet);
                long receiveTime = System.currentTimeMillis();
                handlePacket(packet, receiveTime);
            } catch (IOException e) {
                if (running) {
                    log.error("Error receiving NTP packet", e);
                }
            }
        }
    }

    private void handlePacket(DatagramPacket requestPacket, long receiveTimeMs) {
        try {
            NtpV3Impl message = new NtpV3Impl();
            // We copy the data because NtpV3Impl wraps the array
            message.setDatagramPacket(requestPacket);

            // Basic validation
            if (message.getMode() == NtpV3Packet.MODE_CLIENT) {
                NtpV3Impl response = new NtpV3Impl();

                // Set Header
                response.setMode(NtpV3Packet.MODE_SERVER);
                response.setVersion(NtpV3Packet.VERSION_3);
                response.setStratum(1); // Primary server
                response.setPoll(message.getPoll());
                response.setPrecision(-20); // Arbitrary precision
                response.setReferenceId(0x4C4F434C); // "LOCL"
                response.setReferenceTime(TimeStamp.getCurrentTime());

                // Timestamps
                TimeStamp originateTimestamp = message.getTransmitTimeStamp(); // T1
                TimeStamp receiveTimestamp = TimeStamp.getNtpTime(receiveTimeMs); // T2
                TimeStamp transmitTimestamp = TimeStamp.getCurrentTime(); // T3

                response.setOriginateTimeStamp(originateTimestamp);
                response.setReceiveTimeStamp(receiveTimestamp);
                response.setTransmitTime(transmitTimestamp);

                // Create packet to send
                DatagramPacket responsePacket = response.getDatagramPacket();
                responsePacket.setAddress(requestPacket.getAddress());
                responsePacket.setPort(requestPacket.getPort());

                socket.send(responsePacket);
                log.debug("Sent NTP response to {}:{}", requestPacket.getAddress(), requestPacket.getPort());
            }
        } catch (Exception e) {
            log.error("Error handling NTP packet", e);
        }
    }

    @PreDestroy
    public void stop() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
        log.info("NTP Server stopped");
    }

    // For testing purposes
    public int getPort() {
        return port;
    }

    // For testing purposes
    public void setPort(int port) {
        this.port = port;
    }
}
