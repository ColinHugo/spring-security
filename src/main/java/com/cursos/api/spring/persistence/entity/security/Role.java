package com.cursos.api.spring.persistence.entity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Role {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany( mappedBy = "role", fetch = FetchType.EAGER )
    private List< GrantedPermission > permissions;

}