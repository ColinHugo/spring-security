package com.cursos.api.spring.controller;

import com.cursos.api.spring.dto.auth.AuthenticationRequest;
import com.cursos.api.spring.dto.auth.AuthenticationResponse;
import com.cursos.api.spring.persistence.entity.User;
import com.cursos.api.spring.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping( "/auth" )
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping( "/validate-token" )
    public ResponseEntity< Boolean > validate( @RequestParam String jwt ) {

        boolean isTokenValid = authenticationService.validateToken( jwt );

        return ResponseEntity.ok( isTokenValid );

    }

    @PostMapping( "/authenticate" )
    public ResponseEntity< AuthenticationResponse > authenticate( @Valid @RequestBody AuthenticationRequest authenticationRequest ) {

        AuthenticationResponse rsp = authenticationService.login( authenticationRequest );

        return ResponseEntity.ok( rsp );

    }

    // @PreAuthorize( "hasAnyRole( 'ADMINISTRATOR', 'ASSISTANT_ADMINISTRATOR', 'CUSTOMER' )" )
    @PreAuthorize( "hasAuthority( 'READ_MY_PROFILE' )" )
    @GetMapping( "/profile" )
    public ResponseEntity< User > findMyProfile() {

        User user = authenticationService.findLoggedInUser();

        return ResponseEntity.ok( user );

    }

}