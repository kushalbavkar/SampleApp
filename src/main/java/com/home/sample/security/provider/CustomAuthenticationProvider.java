package com.home.sample.security.provider;

import com.home.sample.utils.UserUtils;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String userName = authentication.getName();
        final String password = authentication.getCredentials().toString();

        final User user = UserUtils.getUser(userName)
                .orElseThrow(() -> new BadCredentialsException("Authentication Failed!"));
        
        if (user.getUsername().equals(userName) && user.getPassword().equals(password))
            return new UsernamePasswordAuthenticationToken(userName, password);
        else
            throw new BadCredentialsException("Authentication Failed!");
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return clazz.equals(UsernamePasswordAuthenticationToken.class);
    }
}
