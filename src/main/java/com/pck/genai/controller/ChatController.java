package com.pck.genai.controller;

import com.pck.genai.dto.ChatRequest;
import com.pck.genai.config.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    ApiConfig apiConfig;

    @PostMapping("/ask")
    public ResponseEntity<String> chatWithGPT(@RequestBody ChatRequest request) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = apiConfig.getHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            System.out.println("Chatcontroller ..... "+headers);
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(request);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, entity, String.class);

            JsonNode responseJson = objectMapper.readTree(response.getBody());
            String aiResponse = responseJson.path("choices").get(0).path("message").path("content").asText();

            return ResponseEntity.ok(aiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}

