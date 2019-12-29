package com.home.sample.security.provider;

import javax.annotation.PostConstruct;

import com.home.sample.service.MyUserDetailsService;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class MockAuthProvider extends PreAuthenticatedAuthenticationProvider {
    private final MyUserDetailsService userDetailsService;

    public MockAuthProvider(final MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    public void init() {
        final UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>();
        wrapper.setUserDetailsService(userDetailsService);
        setPreAuthenticatedUserDetailsService(wrapper);
    }
}
