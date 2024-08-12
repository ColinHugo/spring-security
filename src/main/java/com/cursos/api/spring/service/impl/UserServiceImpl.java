package com.cursos.api.spring.service.impl;

import com.cursos.api.spring.dto.SaveUser;
import com.cursos.api.spring.exception.InvalidPasswordException;
import com.cursos.api.spring.exception.ObjectNotFoundException;
import com.cursos.api.spring.persistence.entity.security.Role;
import com.cursos.api.spring.persistence.entity.security.User;
import com.cursos.api.spring.persistence.repository.security.UserRepository;
import com.cursos.api.spring.service.RoleService;
import com.cursos.api.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleService roleService;

    @Override
    public User registerOneCustomer( SaveUser newUser ) {

        validatePassword( newUser );

        Role role = roleService
                .findDefaultRole()
                .orElseThrow( () -> new ObjectNotFoundException( "Role not found. Default Role" ) );

        User user = User
                .builder()
                .username( newUser.getUsername() )
                .name( newUser.getName() )
                .password( passwordEncoder.encode( newUser.getPassword() ) )
                .role( role )
                .build();

        return userRepository.save( user );

    }

    @Override
    public User findOneByUsername( String username ) {
        return userRepository
                .findByUsername( username )
                .orElseThrow( () -> new ObjectNotFoundException( "User not found" ));
    }

    private void validatePassword( SaveUser dto ) {

        if ( !StringUtils.hasText( dto.getPassword() ) || !StringUtils.hasText( dto.getRepeatedPassword() ) ) {
            throw new InvalidPasswordException( "Passwords don't match" );
        }

        if( !dto.getPassword().equals( dto.getRepeatedPassword() ) ) {
            throw new InvalidPasswordException( "Passwords don't match" );
        }

    }

}