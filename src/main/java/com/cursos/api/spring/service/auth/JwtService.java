package com.cursos.api.spring.service.auth;

import com.cursos.api.spring.persistence.entity.security.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JwtService {

    @Value( "${security.jwt.expiration-in-minutes}" )
    private Long EXPIRATION_IN_MINUTES;

    @Value( "${security.jwt.secret_key}" )
    private String SECRET_KEY;

    public String generateToken( User user ) {

        Date issuedAt = new Date( System.currentTimeMillis() );
        Date expiration = new Date( ( EXPIRATION_IN_MINUTES * 60 * 1000 ) + issuedAt.getTime() );

        return Jwts
                .builder()
                .header()
                .type( "JWT" )
                .and()
                .subject( user.getUsername() )
                .issuedAt( issuedAt )
                .expiration( expiration )
                .claims( generateExtraClaims( user ) )
                .signWith( generateKey(), Jwts.SIG.HS256 )
                .compact();

    }

    private Map< String, Object > generateExtraClaims( User user ) {

        Map< String, Object > extraClaims = new HashMap<>();
        extraClaims.put( "id", user.getId() );
        extraClaims.put( "name", user.getName() );
        extraClaims.put( "role", user.getRole().getName() );
        extraClaims.put( "authorities", user.getAuthorities() );

        return extraClaims;

    }

    private SecretKey generateKey() {

        byte[] passwordDecoded = Decoders.BASE64.decode( SECRET_KEY );

        return Keys.hmacShaKeyFor( passwordDecoded );

    }

    public String extractUsername( String jwt ) {
        return extractAllClaims( jwt ).getSubject();
    }

    public List< SimpleGrantedAuthority > extractAuthorities( String jwt ) {

        Claims claims = extractAllClaims( jwt );
        List< Map< String, String > > authorities = claims.get("authorities", List.class );

        return authorities
                .stream()
                .map( map -> new SimpleGrantedAuthority(map.get("authority")) )
                .toList();

    }

    public Claims extractAllClaims( String jwt ) {
        return Jwts
                .parser()
                .verifyWith( generateKey() )
                .build()
                .parseSignedClaims( jwt )
                .getPayload();
    }

}