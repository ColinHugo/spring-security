package com.cursos.api.spring.config.security;

import com.cursos.api.spring.config.security.filter.JwtAuthenticationFilter;
import com.cursos.api.spring.persistence.util.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
// @EnableMethodSecurity
public class HttpSecurityConfig {

    private final AuthorizationManager< RequestAuthorizationContext > authorizationManager;

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    private final AuthenticationProvider daoAuthProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {

        return http
                .csrf( AbstractHttpConfigurer::disable )
                .sessionManagement( sessConfig -> sessConfig.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
                .authenticationProvider( daoAuthProvider )
                .addFilterBefore( jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class )
                // .authorizeHttpRequests( HttpSecurityConfig::buildRequestMatchers )
                // .authorizeHttpRequests( HttpSecurityConfig::buildRequestMatchersV2 )
                .authorizeHttpRequests( authReqConfig -> authReqConfig.anyRequest().access( authorizationManager ) )
                .exceptionHandling( exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint( authenticationEntryPoint );
                    exceptionHandling.accessDeniedHandler( accessDeniedHandler );
                } )
                .headers( h -> h.frameOptions( HeadersConfigurer.FrameOptionsConfig::sameOrigin ) )
                .build();

    }

    private static void buildRequestMatchers( AuthorizeHttpRequestsConfigurer< HttpSecurity >.AuthorizationManagerRequestMatcherRegistry authReqConfig ) {

        // Autorización de endpoints de productos

        authReqConfig.requestMatchers( HttpMethod.GET, "/products" )
                // .hasAuthority( RolePermission.READ_ALL_PRODUCTS.name() );
                .hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name() );

        // authReqConfig.requestMatchers( HttpMethod.GET, "/products/{productId}" )
        authReqConfig.requestMatchers( RegexRequestMatcher.regexMatcher( HttpMethod.GET, "/products/[0-9]*" ) )
                // .hasAuthority( RolePermission.READ_ONE_PRODUCT.name() );
                .hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name() );

        authReqConfig.requestMatchers( HttpMethod.POST, "/products" )
                // .hasAuthority( RolePermission.CREATE_ONE_PRODUCT.name() );
                .hasRole( RoleEnum.ADMINISTRATOR.name() );

        authReqConfig.requestMatchers( HttpMethod.PUT, "/products/{productId}" )
                // .hasAuthority( RolePermission.UPDATE_ONE_PRODUCT.name() );
                .hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name() );

        authReqConfig.requestMatchers( HttpMethod.PUT, "/products/{productId}/disabled" )
                // .hasAuthority( RolePermission.DISABLE_ONE_PRODUCT.name() );
                .hasRole( RoleEnum.ADMINISTRATOR.name() );

        // Autorización de endpoints de categories

        authReqConfig.requestMatchers( HttpMethod.GET, "/categories" )
                // .hasAuthority( RolePermission.READ_ALL_CATEGORIES.name() );
                .hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name() );

        authReqConfig.requestMatchers( HttpMethod.GET, "/categories/{categoryId}" )
                // .hasAuthority( RolePermission.READ_ONE_CATEGORY.name() );
                .hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name() );

        authReqConfig.requestMatchers( HttpMethod.POST, "/categories" )
                // .hasAuthority( RolePermission.CREATE_ONE_CATEGORY.name() );
                .hasRole( RoleEnum.ADMINISTRATOR.name() );

        authReqConfig.requestMatchers( HttpMethod.PUT, "/categories/{categoryId}" )
                // .hasAuthority( RolePermission.UPDATE_ONE_CATEGORY.name() );
                .hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name() );

        authReqConfig.requestMatchers( HttpMethod.PUT, "/categories/{categoryId}/disabled" )
                // .hasAuthority( RolePermission.DISABLE_ONE_CATEGORY.name() );
                .hasRole( RoleEnum.ADMINISTRATOR.name() );

        // Autorización de endpoints de customers
        authReqConfig.requestMatchers( HttpMethod.POST, "/customers" )
                .hasRole( RoleEnum.ADMINISTRATOR.name() );

        // PROFILE

        authReqConfig.requestMatchers( HttpMethod.GET, "/auth/profile" )
                // .hasAuthority( RolePermission.READ_MY_PROFILE.name() );
                .hasAnyRole( RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name(), RoleEnum.CUSTOMER.name() );

        // Autorización de endpoints públicos

        authReqConfig.requestMatchers( HttpMethod.GET, "/customers" ).permitAll();
        authReqConfig.requestMatchers( HttpMethod.POST, "/auth/authenticate" ).permitAll();
        authReqConfig.requestMatchers( HttpMethod.GET, "/auth/validate-token" ).permitAll();

        authReqConfig.requestMatchers( "/h2-console/**" ).permitAll();

        authReqConfig.anyRequest().authenticated();

    }

    private static void buildRequestMatchersV2( AuthorizeHttpRequestsConfigurer< HttpSecurity >.AuthorizationManagerRequestMatcherRegistry authReqConfig ) {

        // Autorización de endpoints públicos

        authReqConfig.requestMatchers( HttpMethod.GET, "/customers" ).permitAll();
        authReqConfig.requestMatchers( HttpMethod.POST, "/auth/authenticate" ).permitAll();
        authReqConfig.requestMatchers( HttpMethod.GET, "/auth/validate-token" ).permitAll();

        authReqConfig.requestMatchers( "/h2-console/**" ).permitAll();

        authReqConfig.anyRequest().authenticated();

    }

}