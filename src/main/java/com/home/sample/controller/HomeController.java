package com.home.sample.controller;

import java.io.IOException;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public void redirect(final HttpServletResponse response) throws IOException {
        response.sendRedirect("/home");
    }

    @GetMapping("/home")
    public String index() {
        return ("<h1>Welcome</h1>");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(final HttpServletRequest request, final HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        invalidateSession(request.getSession());

        Stream.of(request.getCookies()).forEach(cookie -> cookie.setMaxAge(0));
    }

    private void invalidateSession(final HttpSession session) {
        if (session != null)
            session.invalidate();
    }
}
