package com.compus.placementmanagement;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin-auth")
public class AdminAuthController {

    private final AdminLoginService adminLoginService;

    public AdminAuthController(
            AdminLoginService adminLoginService) {

        this.adminLoginService = adminLoginService;
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        return adminLoginService.login(
                request.getEmail(),
                request.getPassword()
        );
    }
}