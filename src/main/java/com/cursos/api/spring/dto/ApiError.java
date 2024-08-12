package com.cursos.api.spring.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {

    private String backendMessage;
    private String message;
    private String url;
    private String method;
    private LocalDateTime timestamp;

}