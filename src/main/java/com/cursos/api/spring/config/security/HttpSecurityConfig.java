package com.cursos.api.spring.config.security;

import com.cursos.api.spring.config.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity // Arranca la creacion de ciertos componentes y configura por default
// @EnableMethodSecurity
@Configuration
public class HttpSecurityConfig {

    private final AuthorizationManager< RequestAuthorizationContext > authorizationManager;
    private final AuthenticationProvider daoAuthProvider;

    // Se ejecutan con coincidencia por solicitud
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {

        return http
                .csrf( AbstractHttpConfigurer::disable )
                .sessionManagement( smc -> smc.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
                .authenticationProvider( daoAuthProvider )
                .addFilterBefore( jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class )
                // .authorizeHttpRequests( HttpSecurityConfig::buildRequestMatchers )
                // .authorizeHttpRequests( HttpSecurityConfig::buildRequestMatchersV2 )
                .authorizeHttpRequests( hrc -> hrc
                        .requestMatchers( "/h2-console/**" ).permitAll()
                        .anyRequest().access( authorizationManager )
                )
                .exceptionHandling( ehc -> ehc
                        .authenticationEntryPoint( authenticationEntryPoint )
                        .accessDeniedHandler( accessDeniedHandler )
                )
                .headers( hc -> hc.frameOptions( HeadersConfigurer.FrameOptionsConfig::sameOrigin ) )
                .build();

    }

    /* private static void buildRequestMatchers( AuthorizeHttpRequestsConfigurer< HttpSecurity >.AuthorizationManagerRequestMatcherRegistry requests ) {

        requests
                // Autorizacion endpoints productos
                // .requestMatchers( GET, "/products").hasAuthority( READ_ALL_PRODUCTS.getPermission() )
                .requestMatchers( GET, "/products").hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name() )
                // .requestMatchers( GET, "/products/{productId}").hasAuthority( READ_ONE_PRODUCT.getPermission() )
                .requestMatchers( GET, "/products/{productId}").hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name() )
                // .requestMatchers( POST, "/products").hasAuthority( CREATE_ONE_PRODUCT.getPermission() )
                .requestMatchers( POST, "/products").hasRole( RoleEnum.ADMINISTRATOR.name() )
                // .requestMatchers( PUT, "/products/{productId}").hasAuthority( UPDATE_ONE_PRODUCT.getPermission() )
                .requestMatchers( PUT, "/products/{productId}").hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name() )
                // .requestMatchers( PUT, "/products/{productId}/disabled").hasAuthority( DISABLE_ONE_PRODUCT.getPermission() )
                .requestMatchers( PUT, "/products/{productId}/disabled").hasRole( RoleEnum.ADMINISTRATOR.name() )

                // Autorizacion endpoints categories
                // .requestMatchers( GET, "/categories").hasAuthority( READ_ALL_CATEGORIES.getPermission() )
                .requestMatchers( GET, "/categories").hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name() )
                // .requestMatchers( GET, "/categories/{categoryId}").hasAuthority( READ_ONE_CATEGORY.getPermission() )
                .requestMatchers( GET, "/categories/{categoryId}").hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name() )
                // .requestMatchers( POST, "/categories").hasAuthority( CREATE_ONE_CATEGORY.getPermission() )
                .requestMatchers( POST, "/categories").hasRole( RoleEnum.ADMINISTRATOR.name() )
                // .requestMatchers( PUT, "/categories/{categoryId}").hasAuthority( UPDATE_ONE_CATEGORY.getPermission() )
                .requestMatchers( PUT, "/categories/{categoryId}").hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name() )
                // .requestMatchers( PUT, "/categories/{categoryId}/disabled").hasAuthority( DISABLE_ONE_CATEGORY.getPermission() )
                .requestMatchers( PUT, "/categories/{categoryId}/disabled").hasRole( RoleEnum.ADMINISTRATOR.name() )

                // .requestMatchers( GET, "/auth/profile").hasAuthority( READ_MY_PROFILE.getPermission() )
                .requestMatchers( GET, "/auth/profile").hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name(), RoleEnum.CUSTOMER.name() )

                // endpoints públicos
                .requestMatchers( "/customers").permitAll()
                .requestMatchers( POST, "/auth/authenticate").permitAll()
                .requestMatchers( "/h2-console/**").permitAll()

                .anyRequest().authenticated();

    }

    private static void buildRequestMatchersV2( AuthorizeHttpRequestsConfigurer< HttpSecurity >.AuthorizationManagerRequestMatcherRegistry requests ) {

        // Autorización de endpoints públicos

        requests
                .requestMatchers( GET, "/customers" ).permitAll()
                .requestMatchers( POST, "/auth/authenticate" ).permitAll()
                .requestMatchers( GET, "/auth/validate-token" ).permitAll()

                .requestMatchers( "/h2-console/**" ).permitAll()
                .anyRequest().authenticated();

    } */

}