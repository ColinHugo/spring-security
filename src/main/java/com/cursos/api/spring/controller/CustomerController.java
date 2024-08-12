package com.cursos.api.spring.controller;

import com.cursos.api.spring.dto.RegisteredUser;
import com.cursos.api.spring.dto.SaveUser;
import com.cursos.api.spring.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping( "/customers" )
public class CustomerController {

    private final AuthenticationService authenticationService;

    @PreAuthorize( "hasRole( 'ADMINISTRATOR' )" )
    @PostMapping
    public ResponseEntity< RegisteredUser > registerOne( @Valid @RequestBody SaveUser newUser ) {

        RegisteredUser registeredUser = authenticationService.registerOneCustomer( newUser );

        return ResponseEntity.status( HttpStatus.CREATED ).body( registeredUser );

    }

    @GetMapping
    public ResponseEntity< String > getCustomers() {
        return ResponseEntity.ok( "Usuarios" );
    }

}