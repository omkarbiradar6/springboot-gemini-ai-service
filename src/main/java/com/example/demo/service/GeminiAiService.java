package com.example.demo.service;

import com.example.demo.config.GeminiConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GeminiAiService {

    private final GeminiConfig geminiConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public GeminiAiService(GeminiConfig geminiConfig) {
        this.geminiConfig = geminiConfig;
    }
    @SuppressWarnings("unchecked")
    public String askGemini(String websiteContent, String question) {

        String url =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
            + geminiConfig.getApiKey();

        Map<String, Object> body = Map.of(
            "contents", new Object[]{
                Map.of(
                    "parts", new Object[]{
                        Map.of(
                            "text",
                            "Website Content:\n" + websiteContent +
                            "\n\nQuestion:\n" + question
                        )
                    }
                )
            }
        );

        Map<String, Object> response =
                restTemplate.postForObject(url, body, Map.class);

        try {
            var candidates = (java.util.List<Map<String, Object>>) response.get("candidates");
            var content = (Map<String, Object>) candidates.get(0).get("content");
            var parts = (java.util.List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");

        } catch (Exception e) {
            return "AI failed to generate answer";
        }
    
    }
}
