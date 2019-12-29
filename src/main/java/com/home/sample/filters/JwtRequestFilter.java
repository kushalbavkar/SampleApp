package com.home.sample.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.home.sample.service.MyUserDetailsService;
import com.home.sample.utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile("prod")
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String userName = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userName = jwtUtils.extractUserName(token);
        }

        setAuthentication(userName, token, request);
        chain.doFilter(request, response);
    }

    private void setAuthentication(final String userName, final String token, final HttpServletRequest request) {
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (!isValidToken(token, request))
                log.error("Invalid token supplied: \n{}", token);
        } else if (userName == null)
            log.error("User is null {}", userName);
        else
            log.info("User {} is already loaded in SecurityContextHolder", userName);
    }

    private boolean isValidToken(final String token, final HttpServletRequest request) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(token);

        if (!jwtUtils.validateToken(token, userDetails))
            return false;

        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        return true;
    }
}