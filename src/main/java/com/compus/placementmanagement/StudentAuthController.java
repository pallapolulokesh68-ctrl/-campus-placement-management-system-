package com.compus.placementmanagement;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student-auth")
public class StudentAuthController {

    private final StudentLoginService studentLoginService;

    public StudentAuthController(StudentLoginService studentLoginService) {
        this.studentLoginService = studentLoginService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        return studentLoginService.login(
                request.getEmail(),
                request.getPassword()
        );
    }
}