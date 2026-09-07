package com.compus.placementmanagement;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthService(
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    // =========================
    // COMPANY LOGIN
    // =========================

    public CompanyLoginResponse login(
            LoginRequest request) {

        CompanyLoginResponse response =
                new CompanyLoginResponse();


        // Find company
        Company company =
                companyRepository
                        .findByEmail(request.getEmail())
                        .orElse(null);


        // Company not found
        if (company == null) {

            response.setSuccess(false);
            response.setMessage(
                    "Invalid email or password"
            );

            return response;
        }


        // Check password
        if (!passwordEncoder.matches(
                request.getPassword(),
                company.getPassword())) {

            response.setSuccess(false);
            response.setMessage(
                    "Invalid email or password"
            );

            return response;
        }


        // Give default role
        if (company.getRole() == null ||
                company.getRole().isBlank()) {

            company.setRole("COMPANY");
            companyRepository.save(company);
        }


        // Generate JWT
        String token =
                jwtService.generateToken(
                        company.getId(),
                        company.getEmail(),
                        company.getRole()
                );


        // Successful response
        response.setSuccess(true);

        response.setMessage(
                "Company login successful"
        );

        response.setCompanyId(
                company.getId()
        );

        response.setCompanyName(
                company.getName()
        );

        response.setToken(token);


        return response;
    }
}