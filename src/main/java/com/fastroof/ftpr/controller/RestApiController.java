package com.fastroof.ftpr.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @RequestMapping("/userFunction")
    public String userFunction() {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString().equals("ROLE_USER")){
            return "You are a user!";
        }
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString().equals("ROLE_MODERATOR")){
            return "You are a moderator!";
        }
        return "You are not a user at all!";
    }

}
