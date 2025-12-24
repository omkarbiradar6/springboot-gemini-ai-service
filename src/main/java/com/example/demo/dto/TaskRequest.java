package com.example.demo.dto;

public class TaskRequest {

    private String url;
    private String question;

    // REQUIRED: No-arg constructor
    public TaskRequest() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
