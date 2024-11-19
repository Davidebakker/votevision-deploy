package com.election.backendjava.repositories;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieRepository {

    public void deleteJwtCookie(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(false);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
    }

    public void deleteRoleCookie(HttpServletResponse response) {
        Cookie roleCookie = new Cookie("role", null);
        roleCookie.setHttpOnly(false);
        roleCookie.setSecure(false);
        roleCookie.setPath("/");
        roleCookie.setMaxAge(0);
        response.addCookie(roleCookie);
    }

    public void addJwtToCookie(HttpServletResponse response, String jwtToken) {
        Cookie jwtCookie = new Cookie("jwt", jwtToken);
        jwtCookie.setHttpOnly(false);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(jwtCookie);
    }

    public void addRoleToCookie(HttpServletResponse response, String role) {
        Cookie roleCookie = new Cookie("role", role);
        roleCookie.setHttpOnly(false);
        roleCookie.setSecure(false);
        roleCookie.setPath("/");
        roleCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(roleCookie);
    }
}
