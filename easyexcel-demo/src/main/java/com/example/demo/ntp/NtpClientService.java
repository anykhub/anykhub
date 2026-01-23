package com.example.demo.ntp;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Service
public class NtpClientService {

    private static final Logger logger = LoggerFactory.getLogger(NtpClientService.class);

    public Map<String, Object> getNtpTime(String host, int port) {
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(5000);
        Map<String, Object> result = new HashMap<>();

        try {
            client.open();
            InetAddress inetAddress = InetAddress.getByName(host);
            TimeInfo timeInfo = client.getTime(inetAddress, port);

            // Compute details to calculate offset/delay
            timeInfo.computeDetails();

            result.put("success", true);
            result.put("offset", timeInfo.getOffset());
            result.put("delay", timeInfo.getDelay());
            result.put("time", timeInfo.getMessage().getTransmitTimeStamp().getDate());
            result.put("server", host);
            result.put("port", port);

            logger.info("NTP Sync successful: offset={}, delay={}", timeInfo.getOffset(), timeInfo.getDelay());

        } catch (Exception e) {
            logger.error("NTP Sync failed", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        } finally {
            client.close();
        }

        return result;
    }
}
