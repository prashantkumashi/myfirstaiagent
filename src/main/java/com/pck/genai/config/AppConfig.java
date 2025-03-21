package com.pck.genai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAIConfig {

    @Value("${openai.api.key}")
    private String openApiKey;

    public String getOpenApiKey() {
        return openApiKey;
    }
}
