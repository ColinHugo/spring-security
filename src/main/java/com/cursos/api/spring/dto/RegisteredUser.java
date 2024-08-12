package com.cursos.api.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RegisteredUser {

    private Long id;
    private String username;
    private String name;
    private String role;
    private String jwt;

}