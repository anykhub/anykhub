package com.example.demo.ntp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NtpIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testNtpSyncEndpoint() {
        String url = "http://localhost:" + port + "/ntp/sync";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);

        // Check success flag
        assertTrue((Boolean) body.get("success"), "NTP sync should be successful");

        // Check that offset and delay are present (numbers)
        assertNotNull(body.get("offset"));
        assertNotNull(body.get("delay"));

        // Verify server info
        assertEquals("localhost", body.get("server"));
    }
}
