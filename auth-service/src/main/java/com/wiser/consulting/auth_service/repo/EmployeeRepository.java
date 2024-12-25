package com.wiser.consulting.auth_service.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wiser.consulting.auth_service.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByPhone(String phone);

}
