package com.cursos.api.spring.service.impl;

import com.cursos.api.spring.dto.SaveUser;
import com.cursos.api.spring.exception.InvalidPasswordException;
import com.cursos.api.spring.exception.ObjectNotFoundException;
import com.cursos.api.spring.persistence.entity.User;
import com.cursos.api.spring.persistence.repository.UserRepository;
import com.cursos.api.spring.persistence.util.Role;
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

    @Override
    public User registerOneCustomer( SaveUser newUser ) {

        validatePassword( newUser );

        User user = User
                .builder()
                .username( newUser.getUsername() )
                .name( newUser.getName() )
                .password( passwordEncoder.encode( newUser.getPassword() ) )
                .role( Role.CUSTOMER)
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