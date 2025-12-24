package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;

@Service
public class TaskWorkerService {

    private final TaskRepository taskRepository;
    private final WebScraperService webScraperService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public TaskWorkerService(TaskRepository taskRepository,
                             WebScraperService webScraperService) {
        this.taskRepository = taskRepository;
        this.webScraperService = webScraperService;
    }

    public void processTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        try {
            // 1️⃣ Mark IN_PROGRESS
            task.setStatus("IN_PROGRESS");
            taskRepository.save(task);

            // 2️⃣ Scrape website
            String websiteContent = webScraperService.scrapeWebsite(task.getUrl());
            if (websiteContent.length() > 4000) {
                websiteContent = websiteContent.substring(0, 4000);
            }

            // 3️⃣ Build AI prompt
            String prompt = """
                    Use the following website content to answer the question.

                    Website URL:
                    %s

                    Website Content:
                    %s

                    Question:
                    %s
                    """.formatted(task.getUrl(), websiteContent, task.getQuestion());

            // 4️⃣ Gemini request body (CORRECT FORMAT)
            Map<String, Object> body = Map.of(
                "contents", List.of(
                    Map.of(
                        "parts", List.of(
                            Map.of("text", prompt)
                        )
                    )
                )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, headers);

            // 5️⃣ Call Gemini API
            String response = restTemplate.postForObject(
                    apiUrl + "?key=" + apiKey,
                    entity,
                    String.class
            );

            // 6️⃣ Extract AI answer (simple & safe)
            String aiAnswer = response != null && response.contains("\"text\"")
                    ? response.substring(response.indexOf("\"text\":\"") + 8).split("\"")[0]
                    : "No answer generated";

            // 7️⃣ Save result
            task.setAnswer(aiAnswer);
            task.setStatus("COMPLETED");
            taskRepository.save(task);

        } catch (Exception e) {
            task.setStatus("FAILED");
            task.setAnswer(e.getMessage());
            taskRepository.save(task);
            e.printStackTrace();
        }
    }
}
