package com.wiser.consulting.auth_service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeDto {
    private String uid;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String employmentDate;
    private String country;
    private String branch;
    private String department;
    private String role;
    private String office;
    private String status;
}
