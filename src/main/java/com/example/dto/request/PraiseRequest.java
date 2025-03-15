package com.example.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PraiseRequest {
    private Long teacherId;
    private Long studentId;
    private String message;
}
