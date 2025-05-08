package com.cursos.api.spring.config.security.filter;

import com.cursos.api.spring.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException {

        log.info( "Iniciando JWT Authentication Filter" );

        // 1.- Obtener encabezado http llamado Authorization
        String authorizationHeader = request.getHeader( HttpHeaders.AUTHORIZATION );

        if ( !StringUtils.hasText( authorizationHeader ) || !authorizationHeader.startsWith( "Bearer ") ) {
            filterChain.doFilter( request, response );
            return;
        }

        // 2.- Obtener token JWT desde el encabezado
        String jwtToken = authorizationHeader.split( " " )[ 1 ];

        // 3.- Obtener el subject/username desde el token, valida el formato del token, firma y fecha de expiración
        String username = jwtService.extractUsername( jwtToken );
        List< SimpleGrantedAuthority > authorities = jwtService.extractAuthorities( jwtToken );

        // 4.- Setear objeto Authentication dentro de SecurityContextHolder
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, authorities );

        authToken.setDetails( new WebAuthenticationDetails( request ) );

        SecurityContextHolder.getContext().setAuthentication( authToken );

        // 5.- Ejecutar el resto de filtros
        filterChain.doFilter( request, response );

    }

    @Override
    protected boolean shouldNotFilter( HttpServletRequest request ) throws ServletException {
        return request.getServletPath().equals( "/auth/authenticate" );
    }

}