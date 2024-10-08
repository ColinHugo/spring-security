package com.cursos.api.spring.persistence.entity.security;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table( name = "\"user\"" )
public class User implements UserDetails {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( unique = true )
    private String username;
    private String name;
    private String password;

    @ManyToOne
    @JoinColumn( name = "role_id" )
    private Role role;

    @Override
    public Collection< ? extends GrantedAuthority > getAuthorities() {

        if ( role == null || role.getPermissions() == null ) {
            return null;
        }

        List< SimpleGrantedAuthority > authorities = role
                .getPermissions()
                .stream()
                .map( each -> each.getOperation().getName() )
                .map( SimpleGrantedAuthority::new )
                .collect( Collectors.toList() );

        authorities.add( new SimpleGrantedAuthority( "ROLE_" + this.role.getName() ) );

        return authorities;

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}