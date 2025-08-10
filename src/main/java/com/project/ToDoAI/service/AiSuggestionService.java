package com.project.ToDoAI.service;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiSuggestionService {

    @Value("${app.useMockAi}") // configurable via application.properties
    private boolean useMockAi;
    @Value("${openai.api.key}")
    private String apiKey;
    @Value("${openai.api.model}")
    private String gptModel;

    private final OpenAiService openAiClient;

    public String getTaskSuggestion(String taskSummary) {

        if(useMockAi)
            return mockResponse(taskSummary);

        CompletionRequest request = CompletionRequest.builder()
                .model(gptModel)
                .prompt("Here is a list of tasks:\n"
                        + taskSummary +
                        "\n\nBased on their status and urgency, suggest next 5 task should the user do next? " +
                        "Response in JSON format with fields: title, description, status, priority, and due date.")
                .maxTokens(60)
                .temperature(0.7)
                .build();

        try {
            return openAiClient.createCompletion(request).getChoices().get(0).getText().trim();
        } catch (OpenAiHttpException e) {
            if (e.statusCode == 429) {
                throw new RuntimeException("Quota limits reached. Try a simpler request.");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    private String mockResponse(String taskSummary) {
        return "Mock task suggestion for context: " + taskSummary;
    }

}
