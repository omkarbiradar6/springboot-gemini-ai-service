package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Task;
import com.example.demo.service.TaskService;

import com.example.demo.dto.TaskRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    // Constructor Injection
    public TaskController(TaskService taskService)
    {
        this.taskService = taskService;
    }
    
    @PostMapping
    public Task createTask(@RequestBody TaskRequest request) {
        return taskService.createTask(
                request.getUrl(),
                request.getQuestion()
        );
    }

    
    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }


}
