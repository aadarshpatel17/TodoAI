package com.project.ToDoAI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;

}
