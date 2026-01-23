package com.example.demo.service;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;

public class NtpServerServiceTest {

    private NtpServerService ntpServerService;
    private int testPort = 30123;

    @BeforeEach
    public void setUp() throws Exception {
        ntpServerService = new NtpServerService();
        ntpServerService.setPort(testPort);
        ntpServerService.start();
        // Give it a moment to bind
        Thread.sleep(500);
    }

    @AfterEach
    public void tearDown() {
        ntpServerService.stop();
    }

    @Test
    public void testNtpQuery() throws Exception {
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(5000);

        try {
            client.open();
            InetAddress host = InetAddress.getByName("localhost");
            TimeInfo timeInfo = client.getTime(host, testPort);

            Assertions.assertNotNull(timeInfo);
            Assertions.assertNotNull(timeInfo.getMessage());
            Assertions.assertNotNull(timeInfo.getMessage().getTransmitTimeStamp());

            // Check if time is somewhat current (within 1 minute)
            long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
            long now = System.currentTimeMillis();

            Assertions.assertTrue(Math.abs(now - returnTime) < 60000, "Time difference is too large");

            System.out.println("NTP Response Time: " + timeInfo.getMessage().getTransmitTimeStamp().toDateString());

        } finally {
            client.close();
        }
    }
}
