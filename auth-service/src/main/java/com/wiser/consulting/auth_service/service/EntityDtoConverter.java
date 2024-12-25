package com.wiser.consulting.auth_service.service;

import org.springframework.stereotype.Service;

import com.wiser.consulting.auth_service.dto.CreateCustomerDto;
import com.wiser.consulting.auth_service.dto.CreateEmployeeDto;
import com.wiser.consulting.auth_service.dto.CustomerDto;
import com.wiser.consulting.auth_service.dto.EmployeeDto;
import com.wiser.consulting.auth_service.entity.Customer;
import com.wiser.consulting.auth_service.entity.Employee;

@Service
public interface EntityDtoConverter {
    CustomerDto convertCustomerToCustomerDto(Customer customer);
    Customer convertCreateCustomerDtoToCustomer(CreateCustomerDto createCustomerDto);
    EmployeeDto convertEmployeeToEmployeeDto(Employee employee);
    Employee convertCreateEmployeeDtoToEmployee(CreateEmployeeDto employeeDto);
}
