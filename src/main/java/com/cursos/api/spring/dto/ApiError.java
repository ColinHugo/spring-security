package com.cursos.api.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    private LocalDateTime timestamp;

}