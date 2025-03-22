package com.pck.genai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class ApiConfig {

    @Value("${openai.api.key}")
    private String openApiKey;

    @Value("${openai.api.organization}")
    private String openApiOrganization;

    @Value("${openai.api.project}")
    private String openApiProject;

    public String getOpenApiKey() {
        return openApiKey;
    }

    public String getOpenApiOrganization() {
        return openApiOrganization;
    }

    public String getOpenApiProject() {
        return openApiProject;
    }

    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getOpenApiKey());
        headers.set("OpenAI-Organization", getOpenApiOrganization());
        headers.set("OpenAI-Project", getOpenApiProject());
        return headers;
    }
}
