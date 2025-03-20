package com.pck.genai.controller;

import com.pck.genai.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String API_URL = "http://localhost:8080/chat/ask";

    @Test
    public void testChatWithGPT_SimpleQuestion() throws Exception {
        ChatRequest request = new ChatRequest();
        request.setModel("gpt-4-turbo");
        request.setMessages(new com.pck.genai.dto.ChatMessage[]{
                new ChatMessage("system", "You are a helpful AI assistant."),
                new ChatMessage("user", "What is the capital of France?")
        });
        request.setTemperature(0.7);
        request.setMax_tokens(200);
        request.setTop_p(1.0);
        request.setFrequency_penalty(0.0);
        request.setPresence_penalty(0.0);

        ResponseEntity<String> response = sendPostRequest(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testChatWithGPT_ComplexQuestion() throws Exception {
        ChatRequest request = new ChatRequest();
        request.setModel("gpt-4-turbo");
        request.setMessages(new com.pck.genai.dto.ChatMessage[]{
                new ChatMessage("system", "You are a helpful AI assistant."),
                new ChatMessage("user", "Explain quantum mechanics in simple terms.")
        });
        request.setTemperature(0.9);
        request.setMax_tokens(300);
        request.setTop_p(0.8);
        request.setFrequency_penalty(0.2);
        request.setPresence_penalty(0.1);

        ResponseEntity<String> response = sendPostRequest(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private ResponseEntity<String> sendPostRequest(ChatRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
    }
}



