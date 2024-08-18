package com.cursos.api.spring.config.security.authorization;

import com.cursos.api.spring.persistence.entity.security.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class CustomAuthorizationManager implements AuthorizationManager< RequestAuthorizationContext > {

    private final OperationRepository operationRepository;

    @Override
    public AuthorizationDecision check( Supplier<Authentication> authentication, RequestAuthorizationContext requestContext ) {

        HttpServletRequest request = requestContext.getRequest();

        String url = extractUrl( request );
        String httpMethod = request.getMethod();
        boolean isPublic = isPublic( url, httpMethod );

        return new AuthorizationDecision( isPublic );

    }

    private boolean isPublic( String url, String httpMethod ) {

        List< Operation > publicAccessEndpoints = operacionRepopsitory.findByPublicAccess();

        boolean isPublic = publicAccessEndpoints
                .stream()
                .anyMatch( operation -> {

                    String basePath = operation.getModule().getBasePath();
                    Pattern pattern = Pattern.compile( basePath.concat( operation.getPath() ) );
                    Matcher matcher = pattern.matcher( url );

                    return matcher.matches();

                } );

        System.out.println( "IS PUBLIC: " + isPublic );

        return isPublic;

    }

    private String extractUrl( HttpServletRequest request ) {

        String contextPath = request.getContextPath();
        String url = request.getRequestURI();
        url = url.replace( contextPath, "" );
        System.out.println("url = " + url);

        return null;
    }

}