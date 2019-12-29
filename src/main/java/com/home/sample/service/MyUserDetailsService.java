package com.home.sample.service;

import com.home.sample.utils.UserUtils;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Profile({ "prod", "dev", "test" })
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        return UserUtils.getUser(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName.toUpperCase().concat(" not found!")));
    }
}
