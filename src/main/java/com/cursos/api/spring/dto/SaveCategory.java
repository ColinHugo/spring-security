package com.cursos.api.spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveCategory {

    @NotBlank
    private String name;

}