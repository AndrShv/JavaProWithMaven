package com.example.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PraiseResponse {
    private Long id;
    private String message;
    private Long teacherId;
    private Long studentId;
}
