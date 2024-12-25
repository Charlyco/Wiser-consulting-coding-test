package com.wiser.consulting.auth_service.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wiser.consulting.auth_service.dto.CreateCustomerDto;
import com.wiser.consulting.auth_service.dto.CreateEmployeeDto;
import com.wiser.consulting.auth_service.dto.CustomerDto;
import com.wiser.consulting.auth_service.dto.EmployeeDto;
import com.wiser.consulting.auth_service.entity.Customer;
import com.wiser.consulting.auth_service.entity.Employee;
import com.wiser.consulting.auth_service.enums.Department;
import com.wiser.consulting.auth_service.enums.Role;
import com.wiser.consulting.auth_service.service.EntityDtoConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntityDtoConverterImpl implements EntityDtoConverter {
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerDto convertCustomerToCustomerDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setUid(customer.getUid());
        customerDto.setEmail(customer.getEmail());
        customerDto.setFullName(customer.getFullName());
        customerDto.setPhone(customer.getPhone());
        customerDto.setCreatedAt(String.valueOf(customer.getCreatedAt()));
        customerDto.setPermanentAddress(customer.getPermanentAddress());

        return customerDto;
    }

    @Override
    public Customer convertCreateCustomerDtoToCustomer(CreateCustomerDto createCustomerDto) {
        Customer customer = new Customer();
        customer.setEmail(createCustomerDto.getEmail());
        customer.setFullName(createCustomerDto.getFirstName() + " " + createCustomerDto.getLastName());
        customer.setPhone(createCustomerDto.getPhone());
        customer.setPassword(passwordEncoder.encode(createCustomerDto.getPassword()));
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUid(UUID.randomUUID().toString());

        return customer;
    }

    @Override
    public EmployeeDto convertEmployeeToEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setUid(employee.getUid());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setFullName(employee.getFullName());
        employeeDto.setPhone(employee.getPhone());
        employeeDto.setEmploymentDate(String.valueOf(employee.getEmploymentDate()));
        employeeDto.setDepartment(String.valueOf(employee.getDepartment()));
        employeeDto.setBranch(employee.getBranch());
        employeeDto.setOffice(employee.getOffice());
        employeeDto.setCountry(employee.getCountry());
        employeeDto.setRole(String.valueOf(employee.getRole()));
        return employeeDto;
    }

    @Override
    public Employee convertCreateEmployeeDtoToEmployee(CreateEmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setUid(UUID.randomUUID().toString());
        employee.setEmail(employeeDto.getEmail());
        employee.setFullName(employeeDto.getFirstName() + " " + employeeDto.getLastName());
        employee.setPhone(employeeDto.getPhone());
        employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        employee.setDepartment(Department.valueOf(employeeDto.getDepartment()));
        employee.setBranch(employeeDto.getBranch());
        employee.setOffice(employeeDto.getOffice());
        employee.setRole(Role.valueOf(employeeDto.getRole()));
        employee.setCountry(employeeDto.getCountry());
        employee.setEmploymentDate(LocalDate.parse(employeeDto.getEmploymentDate()));
        employee.setEmployeeTag("wellence_" + UUID.randomUUID().toString().substring(0,3));
        return employee;
    }
}
