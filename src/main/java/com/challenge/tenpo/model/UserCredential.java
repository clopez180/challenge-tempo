package com.challenge.tenpo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Getter
@Setter
public class UserCredential implements UserDetails {

    private static final long serialVersionUID = 5348024282615796412L;

    boolean credentialsNonExpired;

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    public UserCredential(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        credentialsNonExpired = true;
    }

    public UserCredential(String username) {
        this.username = username;
    }


    public static UserCredential build(UserData user) {
        return new UserCredential(
                user.getId(),
                user.getUsername(),
                user.getPassword());
    }

    public static UserCredential build(String username) {
        return new UserCredential(username);

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
    public boolean isEnabled() {
        return true;
    }
}
