package com.cursos.api.spring.exception;

import com.cursos.api.spring.dto.ApiError;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler( MethodArgumentNotValidException.class )
    public ResponseEntity< ApiError > handleMethodArgumentNotValidException( MethodArgumentNotValidException exception, HttpServletRequest request ) {

        Map< String, String > errors = new HashMap<>();

        exception
                .getBindingResult()
                .getFieldErrors()
                .forEach( error -> {

                    String nameField = error.getField();
                    String errorMessage = error.getDefaultMessage();

                    errors.put( nameField, errorMessage );

                } );

        ApiError apiError = ApiError
                .builder()
                .backendMessage( exception.getLocalizedMessage() )
                .url( request.getRequestURL().toString() )
                .method( request.getMethod() )
                .message( "Se encontraron errores en los siguientes campos: " + errors )
                .timestamp( LocalDateTime.now() )
                .build();

        log.error( "Detalles del error: " + exception
                .getAllErrors()
                .stream()
                .map( DefaultMessageSourceResolvable::getDefaultMessage )
                .toList()
        );


        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( apiError );

    }

    @ExceptionHandler( InvalidPasswordException.class )
    public ResponseEntity< ApiError > handleInvalidPasswordException( InvalidPasswordException exception, HttpServletRequest request ) {

        ApiError apiError = ApiError
                .builder()
                .backendMessage( exception.getLocalizedMessage() )
                .url( request.getRequestURL().toString() )
                .method( request.getMethod() )
                .message( "Contraseñas no coinciden: " + exception.getMessage() )
                .timestamp( LocalDateTime.now() )
                .build();

        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( apiError );

    }

    @ExceptionHandler( BadCredentialsException.class )
    public ResponseEntity< ApiError > handleBadCredentialsException( BadCredentialsException exception, HttpServletRequest request ) {

        ApiError apiError = ApiError
                .builder()
                .backendMessage( exception.getLocalizedMessage() )
                .url( request.getRequestURL().toString() )
                .method( request.getMethod() )
                .message( "Usuario o contraseña incorrectas: " + exception.getMessage() )
                .timestamp( LocalDateTime.now() )
                .build();

        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( apiError );

    }

    @ExceptionHandler( ExpiredJwtException.class )
    public ResponseEntity< ApiError > handleExpiredJwtException( ExpiredJwtException exception, HttpServletRequest request ) {

        ApiError apiException = ApiError
                .builder()
                .backendMessage( exception.getLocalizedMessage() )
                .url( request.getRequestURL().toString() )
                .method( request.getMethod() )
                .message( "El token ha expirado: " + exception.getMessage() )
                .timestamp( LocalDateTime.now() )
                .build();

        log.error( "Descripción del error: " + apiException );

        return new ResponseEntity<>( apiException, HttpStatus.BAD_REQUEST );

    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity< ApiError > handleGenericException( Exception exception, HttpServletRequest request ) {

        ApiError apiError = ApiError
                .builder()
                .backendMessage( exception.getLocalizedMessage() )
                .url( request.getRequestURL().toString() )
                .method( request.getMethod() )
                .message( "Error interno en el servidor: " + exception.getMessage() )
                .timestamp( LocalDateTime.now() )
                .build();

        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( apiError );

    }

}