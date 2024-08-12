package com.cursos.api.spring.config.security.filter;

import com.cursos.api.spring.persistence.entity.security.User;
import com.cursos.api.spring.service.UserService;
import com.cursos.api.spring.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException {

        // 1.- Obtener encabezado http llamado Authorization
        String authorizationHeader = request.getHeader( "Authorization" );

        if ( !StringUtils.hasText( authorizationHeader ) || !authorizationHeader.startsWith( "Bearer ") ) {
            filterChain.doFilter( request, response );
            return;
        }

        // 2.- Obtener token JWT desde el encabezado
        String jwtToken = authorizationHeader.split( " " )[ 1 ];

        // 3.- Obtener el subject/username desde el token, valida el formeto del token, firma y fecha de expiración
        String username = jwtService.extractUsername( jwtToken );

        // 4.- Settear objecto Authentication dentro de SecurityContextHolder
        User user = userService.findOneByUsername( username );

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, user.getAuthorities() );

        authToken.setDetails( new WebAuthenticationDetails( request ) );

        SecurityContextHolder.getContext().setAuthentication( authToken );

        // 5.- Ejecutar el resto de filtros
        filterChain.doFilter( request, response );

    }

}