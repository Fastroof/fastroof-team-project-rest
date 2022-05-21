package com.fastroof.ftpr.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {
    @RequestMapping("hello")
    public String helloWorld(@RequestParam(value="name", defaultValue="World") String name) {
        return "Hello "+name+"!!";
    }

    @RequestMapping("bighello")
    public String bigHelloWorld(@RequestParam(value="name", defaultValue="WORLD") String name) {
        return "HELLO "+name+"!!";
    }

    @RequestMapping("userFunction")
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
