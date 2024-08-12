package com.cursos.api.spring.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthenticationRequest {

    @NotBlank( message = "El valor de username no puede ir vacio" )
    @NotNull( message = "El parámetro de username es obligatorio" )
    private String username;

    @NotBlank( message = "El valor de password no puede ir vacio" )
    @NotNull( message = "El parámetro de password es obligatorio" )
    private String password;

}