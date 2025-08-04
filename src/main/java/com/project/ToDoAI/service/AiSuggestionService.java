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

    private final OpenAiService openAiClient;

    public String getTaskSuggestion(String taskSummary) {

        if(useMockAi)
            return mockResponse(taskSummary);

        CompletionRequest request = CompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .prompt("Here is a list of tasks:\n" + taskSummary + "\n\nBased on their status and urgency, what task should the user do next? Respond with one helpful sentence.")
                .maxTokens(60)
                .temperature(0.7)
                .build();

        try {
            return openAiClient.createCompletion(request).getChoices().get(0).getText().trim();
        } catch (OpenAiHttpException e) {
            if (e.statusCode == 429) {
                throw new RuntimeException("Unable to fetch AI suggestion due to quota limits. Try writing a simple CRUD operation.");
            }
            throw new RuntimeException("Something went Wrong with openAI!!!");
        }
    }

    private String mockResponse(String taskSummary) {
        return "Mock task suggestion for context: " + taskSummary;
    }

}
