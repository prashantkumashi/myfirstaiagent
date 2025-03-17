package com.pck.genai.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatControllerTest {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL = "http://localhost:8080/chat/ask";

    @Test
    public void testChatWithGPT_SimpleQuestion() throws Exception {
        com.pck.genai.dto.ChatRequest request = new com.pck.genai.dto.ChatRequest();
        request.setModel("gpt-4-turbo");
        request.setMessages(new com.pck.genai.dto.ChatMessage[]{
                new com.pck.genai.dto.ChatMessage("system", "You are a helpful AI assistant."),
                new com.pck.genai.dto.ChatMessage("user", "What is the capital of France?")
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
        com.pck.genai.dto.ChatRequest request = new com.pck.genai.dto.ChatRequest();
        request.setModel("gpt-4-turbo");
        request.setMessages(new com.pck.genai.dto.ChatMessage[]{
                new com.pck.genai.dto.ChatMessage("system", "You are a helpful AI assistant."),
                new com.pck.genai.dto.ChatMessage("user", "Explain quantum mechanics in simple terms.")
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

    private ResponseEntity<String> sendPostRequest(com.pck.genai.dto.ChatRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
    }
}

class ChatRequest {
    private String model;
    private com.pck.genai.dto.ChatMessage[] messages;
    private double temperature;
    private int max_tokens;
    private double top_p;
    private double frequency_penalty;
    private double presence_penalty;

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public com.pck.genai.dto.ChatMessage[] getMessages() { return messages; }
    public void setMessages(com.pck.genai.dto.ChatMessage[] messages) { this.messages = messages; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public int getMax_tokens() { return max_tokens; }
    public void setMax_tokens(int max_tokens) { this.max_tokens = max_tokens; }
    public double getTop_p() { return top_p; }
    public void setTop_p(double top_p) { this.top_p = top_p; }
    public double getFrequency_penalty() { return frequency_penalty; }
    public void setFrequency_penalty(double frequency_penalty) { this.frequency_penalty = frequency_penalty; }
    public double getPresence_penalty() { return presence_penalty; }
    public void setPresence_penalty(double presence_penalty) { this.presence_penalty = presence_penalty; }
}

class ChatMessage {
    private String role;
    private String content;

    public ChatMessage() {}

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
