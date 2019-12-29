package com.home.sample.security;

import com.home.sample.filters.MockAuthFilter;
import com.home.sample.security.provider.MockAuthProvider;
import com.home.sample.service.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Profile("test")
public class MockSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).invalidSessionUrl("/");

        http.csrf().disable().authorizeRequests().anyRequest().authenticated();

        http.addFilterBefore(ssoFilter(), RequestHeaderAuthenticationFilter.class)
                .authenticationProvider(preAuthProvider(userDetailsService));
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthProvider(final MyUserDetailsService userDetailsService2) {
        return new MockAuthProvider(userDetailsService);
    }

    @Bean
    public RequestHeaderAuthenticationFilter ssoFilter() {
        return new MockAuthFilter(authenticationManager);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/h2-console/**");
    }
}