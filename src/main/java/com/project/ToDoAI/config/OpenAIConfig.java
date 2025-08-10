package com.project.ToDoAI.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OpenAIConfig {

    @Value("${openai.api.key}")
    public String apiKey;
    @Value("${openai.api.url}")
    private String apiUrl;

    @Bean
    public OpenAiService openAiClient() {
        return new OpenAiService(apiKey);
    }

}
