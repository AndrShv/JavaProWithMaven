package com.example.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeworkRequest {
    private String description;
    private String status;
    private String dueDate;
}
