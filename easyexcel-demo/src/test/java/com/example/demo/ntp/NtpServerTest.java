package com.example.demo.ntp;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class NtpServerTest {

    @Value("${ntp.port}")
    private int port;

    @Test
    public void testNtpServer() throws Exception {
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(10000);
        try {
            client.open();
            InetAddress host = InetAddress.getByName("localhost");
            // Retry a few times in case server is just starting up
            TimeInfo info = null;
            for (int i = 0; i < 5; i++) {
                try {
                    info = client.getTime(host, port);
                    break;
                } catch (Exception e) {
                    Thread.sleep(1000);
                }
            }

            assertNotNull(info, "Failed to get NTP response");
            info.computeDetails();

            assertNotNull(info.getMessage(), "NTP message should not be null");
            System.out.println("NTP Response: " + info.getMessage());
            System.out.println("Offset: " + info.getOffset());
            System.out.println("Delay: " + info.getDelay());

        } finally {
            client.close();
        }
    }
}
