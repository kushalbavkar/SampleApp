package com.home.sample.controller;

import com.home.sample.model.AuthenticationRequest;
import com.home.sample.model.AuthenticationResponse;
import com.home.sample.security.provider.CustomAuthenticationProvider;
import com.home.sample.service.MyUserDetailsService;
import com.home.sample.utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Profile("prod")
@Slf4j
public class RestServiceController {

    @Autowired
    private CustomAuthenticationProvider authManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody final AuthenticationRequest request) {
        final Authentication auth = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        log.info("User authentication status {}", auth.isAuthenticated());
        return authResponse(request.getUserName());
    }

    private ResponseEntity<?> authResponse(final String user) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user);
        final String jwt = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}