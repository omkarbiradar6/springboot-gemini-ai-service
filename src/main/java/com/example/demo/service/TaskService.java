package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    // Constructor Injection (YOU already know this 🔥)

    
    private final TaskWorkerService taskWorkerService;

    public TaskService(TaskRepository taskRepository,
                       TaskWorkerService taskWorkerService) {
        this.taskRepository = taskRepository;
        this.taskWorkerService = taskWorkerService;
    }
    
    public Task createTask(String url, String question) {

        Task task = new Task();
        task.setUrl(url);
        task.setQuestion(question);
        task.setStatus("QUEUED");

        Task savedTask = taskRepository.save(task);

        // Push to background queue
        taskWorkerService.processTask(savedTask.getId());

        return savedTask;
    }

    
    public Task getTask(Long id) {
        return taskRepository.findById(id).orElse(null);
    }
    
    public void updateTask(Long id, String status, String answer) {

        Task task = taskRepository.findById(id).orElse(null);

        if (task != null) {
            task.setStatus(status);
            task.setAnswer(answer);
            taskRepository.save(task);
        }
    }



}
