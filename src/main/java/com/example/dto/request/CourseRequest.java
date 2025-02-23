package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String theme;
    @NotBlank
    private String way;
    @NotBlank
    private String finishedTime;
}
