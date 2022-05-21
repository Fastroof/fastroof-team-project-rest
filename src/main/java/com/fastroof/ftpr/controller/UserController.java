package com.fastroof.ftpr.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fastroof.ftpr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fastroof.ftpr.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userRepository.findByEmail(email);
        if (user != null) {
            if (passwordEncoder.matches(password,user.getPassword())) {
                if (user.getRole() == 1) {
                    return getUserJWTToken(email);
                } else if (user.getRole() == 2) {
                    return getModeratorJWTToken(email);
                }

            } else {
                return "Wrong password!";
            }
        }
        return "User does not exist!";

    }

    @PostMapping("/register")
    public String register(@RequestParam("email") String email, @RequestParam("password") String password,
                           @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {

        if (userRepository.findByEmail(email) != null) {
            return "User with this email is already registered";
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(password);
            User user = new User();
            user.setEmail(email);
            user.setPassword(encodedPassword);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole(1);

            userRepository.save(user);

            return "User registered";
        }
    }

    private String getUserJWTToken(String email) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        return buildJWTToken(email, secretKey, grantedAuthorities);
    }

    private String getModeratorJWTToken(String email) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_MODERATOR");

        return buildJWTToken(email, secretKey, grantedAuthorities);
    }

    private String buildJWTToken(String email, String secretKey, List<GrantedAuthority> grantedAuthorities) {
        String token = Jwts
                .builder()
                .setId("fastroofJWT")
                .setSubject(email)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}
