package com.cursos.api.spring.service.impl;

import com.cursos.api.spring.persistence.entity.security.Role;
import com.cursos.api.spring.persistence.repository.security.RoleRepository;
import com.cursos.api.spring.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Value( "${security.default.role}" )
    private String defaultRole;

    @Override
    public Optional< Role > findDefaultRole() {
        return roleRepository.findByName( defaultRole );
    }

}