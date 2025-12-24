package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String url;

    @Column(nullable = false, length = 2000)
    private String question;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(length = 4000)
    private String answer;

    // Required by JPA
    public Task() {}

    // Getters & Setters (NO change needed)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}
