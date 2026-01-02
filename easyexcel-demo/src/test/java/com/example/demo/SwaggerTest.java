package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=Oracle",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.datasource.schema=classpath:test_schema.sql"
})
@AutoConfigureMockMvc
public class SwaggerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSwaggerJsonEndpoint() throws Exception {
        // Verify that the OpenAPI JSON docs are available
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("EasyExcel Demo API")));
    }

    @Test
    public void testSwaggerUiEndpoint() throws Exception {
        // Verify that the Swagger UI HTML is available
        // Note: SpringDoc 1.7.0 usually serves UI at /swagger-ui/index.html
        // And redirects from /swagger-ui.html

        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Swagger UI")));
    }
}
