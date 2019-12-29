package com.home.sample.constants;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppUsers {
    FOO("foo", "foo", new ArrayList<>()),
    JOHN("john", "john", new ArrayList<>());

    private String name;
    private String password;
    private List<GrantedAuthority> roles;
}