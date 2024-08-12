package com.cursos.api.spring.service;

import com.cursos.api.spring.dto.SaveUser;
import com.cursos.api.spring.persistence.entity.User;

public interface UserService {

    User registerOneCustomer( SaveUser newUser );

    User findOneByUsername( String username );

}