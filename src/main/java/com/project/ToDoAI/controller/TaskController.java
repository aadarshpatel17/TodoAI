package com.project.ToDoAI.controller;

import com.project.ToDoAI.dto.TaskRequest;
import com.project.ToDoAI.dto.TaskResponse;
import com.project.ToDoAI.entity.Task;
import com.project.ToDoAI.entity.User;
import com.project.ToDoAI.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public TaskResponse createTask(@RequestBody TaskRequest request) {
        return taskService.createTask(request, getCurrentUser());
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.getUserTasks(getCurrentUser());
    }

    @PostMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        return taskService.updateTask(id, request, getCurrentUser());
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id, getCurrentUser());
    }

    @GetMapping("/suggest")
    public String suggestNextTask() {
        return taskService.suggestNextTask(getCurrentUser());
    }

}
