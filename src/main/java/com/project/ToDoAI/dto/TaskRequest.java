package com.project.ToDoAI.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {

    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;

}
