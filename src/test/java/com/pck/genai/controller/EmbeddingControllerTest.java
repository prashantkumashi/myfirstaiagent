package com.pck.genai.controller;

import com.pck.genai.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmbeddingControllerTest {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL = "http://localhost:8080/embedding/match";

    @Test
    public void testMatchServices() throws Exception {
        ChatRequest request = new ChatRequest();
        request.setMessages(List.of(new ChatMessage("user", "I need scalable compute and storage services")).toArray(new ChatMessage[0]));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<String> matchedIds = objectMapper.readValue(response.getBody(), List.class);
        assertFalse(matchedIds.isEmpty(), "No matching services found");
    }
}

