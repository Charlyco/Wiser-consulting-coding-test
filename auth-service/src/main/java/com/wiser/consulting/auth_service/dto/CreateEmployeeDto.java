package com.wiser.consulting.auth_service.dto;

import lombok.Getter;

@Getter
public class CreateEmployeeDto {
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String country;
    private String branch;
    private String department;
    private String role;
    private String office;
    private String employmentDate;
}
