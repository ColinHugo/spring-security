package com.cursos.api.spring.persistence.entity.security;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GrantedPermission {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne
    @JoinColumn( name = "role_id" )
    private Role role;

    @ManyToOne
    @JoinColumn( name = "operation_id" )
    private Operation operation;

}