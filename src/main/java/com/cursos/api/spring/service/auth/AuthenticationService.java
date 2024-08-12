package com.cursos.api.spring.service.auth;

import com.cursos.api.spring.dto.RegisteredUser;
import com.cursos.api.spring.dto.SaveUser;
import com.cursos.api.spring.dto.auth.AuthenticationRequest;
import com.cursos.api.spring.dto.auth.AuthenticationResponse;
import com.cursos.api.spring.persistence.entity.User;
import com.cursos.api.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final JwtService jwtService;

    public RegisteredUser registerOneCustomer( SaveUser newUser ) {

        User user = userService.registerOneCustomer( newUser );

        RegisteredUser userDto = RegisteredUser
                .builder()
                .id( user.getId() )
                .name( user.getName() )
                .username( user.getUsername() )
                .role( user.getRole().name() )
                .build();

        String jwt = jwtService.generateToken( user, generateExtraClaims( user ) );
        userDto.setJwt( jwt );

        return userDto;

    }

    private Map< String, Object > generateExtraClaims( User user ) {

        Map< String, Object > extraClaims = new HashMap<>();
        extraClaims.put( "name", user.getName() );
        extraClaims.put( "role", user.getRole().name() );
        extraClaims.put( "authorities", user.getAuthorities() );

        return extraClaims;

    }

    public AuthenticationResponse login( AuthenticationRequest authRequest ) {

        Authentication authentication = new UsernamePasswordAuthenticationToken( authRequest.getUsername(), authRequest.getPassword() );

        authenticationManager.authenticate( authentication );

        User user = userService.findOneByUsername( authRequest.getUsername() );

        String jwt = jwtService.generateToken( user, generateExtraClaims( user ) );

        return new AuthenticationResponse( jwt );

    }

    public boolean validateToken( String jwt ) {
        jwtService.extractUsername( jwt );
        return true;
    }

    public User findLoggedInUser() {

        UsernamePasswordAuthenticationToken auth = ( UsernamePasswordAuthenticationToken ) SecurityContextHolder.getContext().getAuthentication();

        String username = ( String ) auth.getPrincipal();

        return userService.findOneByUsername( username );

    }

}