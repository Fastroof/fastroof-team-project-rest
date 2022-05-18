package com.fastroof.ftpr.request;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserRegistrationRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}