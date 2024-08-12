package com.cursos.api.spring.service;

import com.cursos.api.spring.persistence.entity.security.Role;

import java.util.Optional;

public interface RoleService {

    Optional< Role > findDefaultRole();

}
