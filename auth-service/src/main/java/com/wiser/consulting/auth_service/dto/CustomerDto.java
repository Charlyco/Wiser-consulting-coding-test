package com.wiser.consulting.auth_service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerDto {
    private String uid;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String createdAt;
    private String country;
    private String permanentAddress;
    private String status;
}
