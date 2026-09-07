package com.compus.placementmanagement;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentLoginService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public StudentLoginService(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    // =========================
    // STUDENT LOGIN
    // =========================

    public LoginResponse login(
            String email,
            String password) {

        LoginResponse response = new LoginResponse();

        Student student =
                studentRepository
                        .findByEmail(email)
                        .orElse(null);


        // Student not found
        if (student == null) {

            response.setSuccess(false);
            response.setMessage(
                    "Invalid email or password"
            );

            return response;
        }


        // Check password
        if (!passwordEncoder.matches(
                password,
                student.getPassword())) {

            response.setSuccess(false);
            response.setMessage(
                    "Invalid email or password"
            );

            return response;
        }


        // If role is empty, give STUDENT role
        if (student.getRole() == null ||
                student.getRole().isBlank()) {

            student.setRole("STUDENT");
            studentRepository.save(student);
        }


        // Generate JWT
        String token =
                jwtService.generateToken(
                        student.getId(),
                        student.getEmail(),
                        student.getRole()
                );


        // Successful response
        response.setSuccess(true);

        response.setMessage(
                "Student login successful"
        );

        response.setStudentId(
                student.getId()
        );

        response.setStudentName(
                student.getName()
        );

        response.setToken(token);


        return response;
    }
}