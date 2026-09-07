package com.compus.placementmanagement;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyController(
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder) {

        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public Company registerCompany(
            @RequestBody Company company) {

        company.setRole("COMPANY");

        company.setPassword(
                passwordEncoder.encode(
                        company.getPassword()
                )
        );

        return companyRepository.save(company);
    }

    @GetMapping
    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    @GetMapping("/{id}")
    public Company getCompany(
            @PathVariable Long id) {

        return companyRepository
                .findById(id)
                .orElse(null);
    }

    @PutMapping("/{id}")
    public Company updateCompany(
            @PathVariable Long id,
            @RequestBody Company company,
            Authentication authentication) {

        Long loggedInCompanyId =
                (Long) authentication.getPrincipal();

        if (!loggedInCompanyId.equals(id)) {
            throw new RuntimeException(
                    "You can only update your own company profile"
            );
        }

        Company existingCompany =
                companyRepository
                        .findById(id)
                        .orElse(null);

        if (existingCompany == null) {
            return null;
        }

        existingCompany.setName(
                company.getName()
        );

        existingCompany.setEmail(
                company.getEmail()
        );

        if (company.getPassword() != null &&
                !company.getPassword().isBlank()) {

            existingCompany.setPassword(
                    passwordEncoder.encode(
                            company.getPassword()
                    )
            );
        }

        return companyRepository.save(
                existingCompany
        );
    }

    @PutMapping("/{id}/password")
    public Map<String, String> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        Long loggedInCompanyId =
                (Long) authentication.getPrincipal();

        if (!loggedInCompanyId.equals(id)) {
            throw new RuntimeException(
                    "You can only change your own password"
            );
        }

        Company company =
                companyRepository
                        .findById(id)
                        .orElse(null);

        if (company == null) {
            throw new RuntimeException(
                    "Company not found"
            );
        }

        String currentPassword =
                request.get("currentPassword");

        String newPassword =
                request.get("newPassword");

        if (currentPassword == null ||
                currentPassword.isBlank()) {

            throw new RuntimeException(
                    "Current password is required"
            );
        }

        if (newPassword == null ||
                newPassword.isBlank()) {

            throw new RuntimeException(
                    "New password is required"
            );
        }

        if (!passwordEncoder.matches(
                currentPassword,
                company.getPassword())) {

            throw new RuntimeException(
                    "Current password is incorrect"
            );
        }

        if (newPassword.length() < 6) {

            throw new RuntimeException(
                    "New password must contain at least 6 characters"
            );
        }

        company.setPassword(
                passwordEncoder.encode(
                        newPassword
                )
        );

        companyRepository.save(company);

        return Map.of(
                "message",
                "Password changed successfully"
        );
    }

    @DeleteMapping("/{id}")
    public String deleteCompany(
            @PathVariable Long id,
            Authentication authentication) {

        Long loggedInCompanyId =
                (Long) authentication.getPrincipal();

        if (!loggedInCompanyId.equals(id)) {
            throw new RuntimeException(
                    "You can only delete your own company account"
            );
        }

        companyRepository.deleteById(id);

        return "Company deleted successfully";
    }
}