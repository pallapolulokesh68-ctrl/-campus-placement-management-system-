package com.compus.placementmanagement;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminLoginService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AdminLoginService(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(
            String email,
            String password) {

        LoginResponse response =
                new LoginResponse();

        Admin admin =
                adminRepository
                        .findByEmail(email)
                        .orElse(null);

        if (admin == null) {

            response.setSuccess(false);
            response.setMessage(
                    "Invalid email or password"
            );

            return response;
        }

        if (!passwordEncoder.matches(
                password,
                admin.getPassword())) {

            response.setSuccess(false);
            response.setMessage(
                    "Invalid email or password"
            );

            return response;
        }

        if (admin.getRole() == null ||
                admin.getRole().isBlank()) {

            admin.setRole("ADMIN");
            adminRepository.save(admin);
        }

        String token =
                jwtService.generateToken(
                        admin.getId(),
                        admin.getEmail(),
                        admin.getRole()
                );

        response.setSuccess(true);
        response.setMessage(
                "Admin login successful"
        );
        response.setStudentId(admin.getId());
        response.setStudentName(admin.getName());
        response.setToken(token);

        return response;
    }
}