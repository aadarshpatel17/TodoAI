package com.project.ToDoAI.service;

import com.project.ToDoAI.dto.TaskRequest;
import com.project.ToDoAI.dto.TaskResponse;
import com.project.ToDoAI.entity.Task;
import com.project.ToDoAI.entity.User;
import com.project.ToDoAI.repository.TaskRepository;
import com.project.ToDoAI.util.TaskPriority;
import com.project.ToDoAI.util.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final AiSuggestionService openAiService;

    public TaskResponse createTask(TaskRequest request, User user) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.valueOf(request.getStatus()));
        task.setPriority(TaskPriority.valueOf(request.getPriority()));
        task.setDueDate(request.getDueDate());
        task.setCreatedAt(java.time.LocalDateTime.now());
        task.setUser(user);

        System.out.println(task);
        Task saved = taskRepository.save(task);
        System.out.println(saved);
        return mapToResponse(saved);
    }

    public List<TaskResponse> getUserTasks(User user) {
        return taskRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse updateTask(Long id, TaskRequest request, User user) {
        Task task = taskRepository.findByIdAndUser(id, user).orElseThrow(() -> new RuntimeException("Task Not Found!!!"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.valueOf(request.getStatus()));
        task.setPriority(TaskPriority.valueOf(request.getPriority()));
        task.setDueDate(request.getDueDate());

        Task updated = taskRepository.save(task);
        return mapToResponse(updated);
    }

    public void deleteTask(Long id, User user) {
        Task task = taskRepository.findByIdAndUser(id, user).orElseThrow(() -> new RuntimeException("Task Not Found!!!"));
        taskRepository.delete(task);
    }

    public String suggestNextTask(User user) {
        List<Task> tasks = taskRepository.findByUser(user);

        String taskSummary = tasks.stream().map(task -> "- " + task.getTitle() + " (" + task.getStatus() + ")")
                .collect(Collectors.joining("\n"));

        String suggestion = openAiService.getTaskSuggestion(taskSummary);
        System.out.println(suggestion);
        return suggestion;
    }

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus().name(), task.getPriority().name(), task.getDueDate());
    }

}
