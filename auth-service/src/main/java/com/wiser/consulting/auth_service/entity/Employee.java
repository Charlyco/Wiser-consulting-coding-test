package com.wiser.consulting.auth_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.wiser.consulting.auth_service.enums.Department;
import com.wiser.consulting.auth_service.enums.Role;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "employee_id")
@DiscriminatorValue("A")
public class Employee extends User {
    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private Department department;
    private String employeeTag;
    private LocalDate employmentDate;
    private String branch;
    private String country;
    private String office;
    private Role role;
}