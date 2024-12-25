package com.wiser.consulting.auth_service.serviceimpl;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wiser.consulting.auth_service.dto.ApiResponse;
import com.wiser.consulting.auth_service.dto.AuthResponse;
import com.wiser.consulting.auth_service.dto.CreateCustomerDto;
import com.wiser.consulting.auth_service.dto.CreateEmployeeDto;
import com.wiser.consulting.auth_service.dto.CustomerDto;
import com.wiser.consulting.auth_service.dto.EmployeeDto;
import com.wiser.consulting.auth_service.entity.AuthToken;
import com.wiser.consulting.auth_service.entity.Customer;
import com.wiser.consulting.auth_service.entity.Employee;
import com.wiser.consulting.auth_service.entity.User;
import com.wiser.consulting.auth_service.enums.Department;
import com.wiser.consulting.auth_service.enums.Role;
import com.wiser.consulting.auth_service.repo.CustomerRepository;
import com.wiser.consulting.auth_service.repo.EmployeeRepository;
import com.wiser.consulting.auth_service.repo.TokenRepository;
import com.wiser.consulting.auth_service.repo.UserRepository;
import com.wiser.consulting.auth_service.security.JwtService;
import com.wiser.consulting.auth_service.service.AuthService;
import com.wiser.consulting.auth_service.service.EntityDtoConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final TokenRepository tokenRepository;
    private final EmployeeRepository employeeRepository;
    private final AuthenticationManager authenticationManager;
    private final EntityDtoConverter entityDtoConverter;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    @Override
    public AuthResponse loginWithEmailAndPassword(String email, String password, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
              email, password
        ));

        if (authentication.isAuthenticated()) {
            Customer customer = customerRepository.findByEmail(email).orElseThrow();
            String accessToken = jwtService.generateToken(customer);
            saveToken(customer, accessToken);
            CustomerDto customerDto = entityDtoConverter.convertCustomerToCustomerDto(customer);
            log.info("{} successfully authenticated last at {} from {}", customer.getUsername(), LocalDateTime.now(), userAgent);
            return new AuthResponse("Successfully authenticated", true, accessToken, customerDto);
        }
        return new AuthResponse("Authentication failed", false, null, null);
    }

    @Override
    public ApiResponse signOut(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String authToken = request.getHeader("Authorization");
        revokeToken(authToken);
        AuthToken tokenToDelete = tokenRepository.findAuthTokenByToken(authToken).orElseThrow();
        tokenRepository.delete(tokenToDelete);
        log.info("User signed out successfully from {}", userAgent);
        return new ApiResponse("Successfully signed out", 200, true, null);
    }

    @Override
    public ApiResponse createCustomer(CreateCustomerDto createCustomerDto, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (customerRepository.findByEmail(createCustomerDto.getEmail()).isPresent()) {
            return new ApiResponse("Customer with email already exists", 400, false, "Customer already exists");
        } else if (customerRepository.findByPhone(createCustomerDto.getPhone()).isPresent()) {
            return new ApiResponse("Customer with phone already exists", 400, false, "Customer already exists");
        }else {
            Customer customer = entityDtoConverter.convertCreateCustomerDtoToCustomer(createCustomerDto);
            customerRepository.save(customer);
            log.info("Customer {} created successfully from {}", customer.getEmail(), userAgent);
            return new ApiResponse("Successfully created customer account", 200, true, null);
        }
    }

    @Override
    public ApiResponse createEmployee(CreateEmployeeDto createEmployeeDto, HttpServletRequest request) {
        String userToken = request.getHeader("Authorization");
        String loggedInUser = getUserInfoByToken(userToken);
        if (employeeRepository.findByEmail(createEmployeeDto.getEmail()).isPresent()) {
            return new ApiResponse("Employee already exists", 400, false, "Employee already exists");
        } else if (employeeRepository.findByPhone(createEmployeeDto.getPhone()).isPresent()) {
            return new ApiResponse("Phone number already exists", 400, false, "Phone number already exists");
        }else {
            Employee employee = employeeRepository.save(entityDtoConverter.convertCreateEmployeeDtoToEmployee(createEmployeeDto));
            log.info("New employee for {} account created by {} on {}", employee.getFullName(), loggedInUser, LocalDateTime.now());
            return new ApiResponse("Successfully created employee", 200, true, null);
        }
    }


    @Override
    public AuthResponse loginEmployee(String email, String password) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        if (authentication.isAuthenticated()) {
            Employee employee = employeeRepository.findByEmail(email).orElseThrow();
            String accessToken = jwtService.generateToken(employee);
            saveToken(employee, accessToken);
            EmployeeDto employeeDto = entityDtoConverter.convertEmployeeToEmployeeDto(employee);
            log.info("Successful login by {} at {}", employee.getFullName(), LocalDateTime.now());
            return new AuthResponse("Authentication successful", true, accessToken, employeeDto);
        }else {
            return new AuthResponse("Authentication failed", false, null, null);
        }
    }

    @Override
    public Boolean isInventoryAdmin(HttpServletRequest request) {
        String userToken = request.getHeader("Authorization");
        String username = getUserInfoByToken(userToken);
        Employee employee = employeeRepository.findByEmail(username).orElseThrow();
        return employee.getDepartment().equals(Department.Inventory) && employee.getRole().equals(Role.ADMIN);
    }

    @Override
    public ApiResponse getUserByAccessToken(HttpServletRequest request) {
        String userToken = request.getHeader("Authorization");
        String email = getUserInfoByToken(userToken);
        return new ApiResponse("User found", HttpStatus.SC_OK, true, email);
    }

    @Override
    public Boolean isCustomerService(HttpServletRequest request) {
        String userToken = request.getHeader("Authorization");
        String username = getUserInfoByToken(userToken);
        Employee employee = employeeRepository.findByEmail(username).orElseThrow();
        return employee.getDepartment().equals(Department.Cutomer_Support);
    }

    @Override
    public Long getUserIdByAccessToken(HttpServletRequest request) {
        String userToken = request.getHeader("Authorization");
        String email = getUserInfoByToken(userToken);
        User user = userRepository.findUserByEmail(email).orElseThrow();
        return user.getUserId();
    }

    private void saveToken(User user, String accessToken) {
        AuthToken token = new AuthToken();
        token.setToken(accessToken);
        token.setUserId(user.getUserId());
        token.setExpired(false);
        token.setRevoked(false);
        if (tokenRepository.getAllByUserId((user.getUserId())).isEmpty()) {
            tokenRepository.save(token);
        }else {
            List<AuthToken> existingTokens = tokenRepository.getAllByUserId(user.getUserId()).orElseThrow();
            tokenRepository.deleteAll(existingTokens);
            tokenRepository.save(token);
        }
    }

    public void revokeToken(String token) {
        AuthToken tokenToRevoke = tokenRepository.findAuthTokenByToken(token).orElseThrow();
        tokenToRevoke.setRevoked(true);
        tokenRepository.save(tokenToRevoke);
    }

    private String getUserInfoByToken(String userToken) {
        return jwtService.extractUserName(userToken.substring(7));
    }

    private boolean isUserHrAdmin(String userToken) {
        String email = getUserInfoByToken(userToken);
        Employee employee = employeeRepository.findByEmail(email).orElseThrow();
        return (employee.isEnabled() && employee.getDepartment().equals(Department.HR) && employee.getRole() == Role.ADMIN);
    }

    private boolean isUserCompanyAdmin(String userToken) {
        String email = getUserInfoByToken(userToken);
        Employee employee = employeeRepository.findByEmail(email).orElseThrow();
        return (employee.getRole().equals(Role.ADMIN));
    }

}
