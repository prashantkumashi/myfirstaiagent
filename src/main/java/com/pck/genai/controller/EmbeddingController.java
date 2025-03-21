package com.pck.genai.controller;

import com.pck.genai.dto.ChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/embedding")
public class EmbeddingController {

    private static final String EMBEDDING_API_URL = "https://api.openai.com/v1/embeddings";
    private static final String API_KEY = "your_openai_api_key";
    private static final String EMBEDDING_FILE_PATH = "data/aws_filtered_services.json";
    private static List<Map<String, Object>> DATA;

    @Autowired
    AppConfig appConfig;

    static {
        try {
            ClassPathResource resource = new ClassPathResource(EMBEDDING_FILE_PATH);
            String content = new String(Files.readAllBytes(Paths.get(resource.getURI())));
            ObjectMapper objectMapper = new ObjectMapper();
            DATA = objectMapper.readValue(content, List.class);
        } catch (Exception e) {
            DATA = new ArrayList<>();
            System.err.println("Error loading embedding JSON file: " + e.getMessage());
        }
    }

    @PostMapping("/match")
    public ResponseEntity<List<String>> matchServices(@RequestBody ChatRequest request) throws Exception {
        List<String> matchedIds = new ArrayList<>();
        double[] promptEmbedding = getEmbedding(request.getMessages()[0].getContent());

        for (Map<String, Object> service : DATA) {
            String serviceDescription = service.get("type") + " " + service.get("service") + " " + service.get("options").toString();
            double[] serviceEmbedding = getEmbedding(serviceDescription);

            double similarity = cosineSimilarity(promptEmbedding, serviceEmbedding);
            if (similarity > 0.8) {
                matchedIds.add((String) service.get("id"));
            }
        }
        return ResponseEntity.ok(matchedIds);
    }

    private double[] getEmbedding(String text) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + appConfig.getOpenApiKey());

        String requestBody = "{\"input\": \"" + text + "\", \"model\": \"text-embedding-ada-002\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(EMBEDDING_API_URL, HttpMethod.POST, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());
        List<Double> embeddingList;
        embeddingList = new ObjectMapper().convertValue(responseJson.path("data").get(0).path("embedding"), List.class);

        return embeddingList.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
