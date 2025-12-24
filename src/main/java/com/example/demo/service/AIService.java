package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class AIService {

    public String askAI(String websiteContent, String question) {

        // Simulating AI response (FREE API replacement)
        return "AI Answer for question: \"" + question + "\" "
             + "based on website content summary.";
    }
}
