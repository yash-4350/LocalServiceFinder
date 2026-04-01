package com.db.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

    private String firstName;
    private String lastName;
    private  String email;
    private String cellphone;
    private String password;
    private String role;
    private AddressRequest address;
}