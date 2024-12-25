package com.wiser.consulting.auth_service.dto;

import lombok.Getter;

@Getter
public class CreateCustomerDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String country;
}
