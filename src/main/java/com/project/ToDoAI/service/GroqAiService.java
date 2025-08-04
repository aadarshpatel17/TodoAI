package com.project.ToDoAI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroqAiService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.groq.com/openai/v1")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public String getTaskSuggestion(String taskSummary) {
        Map<String, Object> requestBody = Map.of(
                "model", "llama3-70b-8192",
                "messages", List.of(
                        Map.of("role", "system", "content", """
                You are a helpful and concise task planning assistant.\s
                Based on the user's previous tasks and their statuses, suggest 5 high-priority or next-logical tasks they should consider doing next.\s
                Be short and helpful. Only output a numbered list of 5 tasks.
           \s"""),
                        Map.of("role", "user", "content", "Here are my current tasks:\n" + taskSummary)
                ),
                "temperature", 0.7,
                "max_tokens", 150
        );

        try {
            Map response = webClient.post()
                    .uri("/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            return message.get("content").toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to fetch suggestion from Groq AI.";
        }
    }

}
