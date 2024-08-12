package com.cursos.api.spring.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SaveUser {

    @Size( min = 4 )
    private String name;

    private String username;

    @Size( min = 8 )
    private String password;

    @Size( min = 8 )
    private String repeatedPassword;

}