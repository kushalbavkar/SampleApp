package com.home.sample.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.home.sample.service.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Profile("test")
public class MockAuthFilter extends RequestHeaderAuthenticationFilter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Value("${security.mock.name:foo}")
    private String name;

    public MockAuthFilter(final AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        session.setAttribute("content-type", "application/json");

        return userDetailsService.loadUserByUsername(name);
    }
}