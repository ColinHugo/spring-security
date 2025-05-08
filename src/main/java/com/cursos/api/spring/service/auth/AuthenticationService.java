package com.cursos.api.spring.service.auth;

import com.cursos.api.spring.dto.RegisteredUser;
import com.cursos.api.spring.dto.SaveUser;
import com.cursos.api.spring.dto.auth.AuthenticationRequest;
import com.cursos.api.spring.dto.auth.AuthenticationResponse;
import com.cursos.api.spring.persistence.entity.security.User;
import com.cursos.api.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final JwtService jwtService;

    public RegisteredUser registerOneCustomer( SaveUser newUser ) {

        User user = userService.registerOneCustomer( newUser );

        return RegisteredUser
                .builder()
                .id( user.getId() )
                .name( user.getName() )
                .username( user.getUsername() )
                .role( user.getRole().getName() )
                .jwt( jwtService.generateToken( user ) )
                .build();

    }

    public AuthenticationResponse login( AuthenticationRequest authRequest ) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( authRequest.getUsername(), authRequest.getPassword() )
        );

        User user = ( User ) authentication.getPrincipal();

        String jwt = jwtService.generateToken( user );

        return new AuthenticationResponse( jwt );

    }

    public boolean validateToken( String jwt ) {
        jwtService.extractUsername( jwt );
        return true;
    }

    public User findLoggedInUser() {
        return userService.findOneByUsername( SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString() );
    }

}