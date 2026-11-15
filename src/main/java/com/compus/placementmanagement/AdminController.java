
package com.compus.placementmanagement.controller;

import com.compus.placementmanagement.Admin;
import com.compus.placementmanagement.AdminRepository;
import com.compus.placementmanagement.StudentRepository;
import com.compus.placementmanagement.CompanyRepository;
import com.compus.placementmanagement.JobRepository;
import com.compus.placementmanagement.ApplicationRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://campus-placement-management.netlify.app"
})
public class AdminController {

    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_BOOTSTRAP_SECRET:}")
    private String bootstrapSecret;

    public AdminController(
            StudentRepository studentRepository,
            CompanyRepository companyRepository,
            JobRepository jobRepository,
            ApplicationRepository applicationRepository,
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {

        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE FIRST ADMIN
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createAdmin(
            @RequestBody Admin admin,
            @RequestHeader(
                    value = "X-ADMIN-BOOTSTRAP-SECRET",
                    required = false
            ) String providedSecret) {

        Map<String, String> response = new HashMap<>();

        // Verify bootstrap secret
        if (bootstrapSecret == null || bootstrapSecret.isBlank()
                || providedSecret == null
                || !MessageDigest.isEqual(
                bootstrapSecret.getBytes(StandardCharsets.UTF_8),
                providedSecret.getBytes(StandardCharsets.UTF_8))) {

            response.put("message", "Invalid bootstrap secret");

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(response);
        }

        // Allow only the first admin
        if (adminRepository.count() > 0) {

            response.put("message", "Admin account already exists");

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(response);
        }

        // Validate input
        if (admin.getName() == null || admin.getName().isBlank()
                || admin.getEmail() == null || admin.getEmail().isBlank()
                || admin.getPassword() == null || admin.getPassword().isBlank()) {

            response.put("message",
                    "Name, email and password are required");

            return ResponseEntity.badRequest().body(response);
        }

        // Normalize email
        admin.setEmail(admin.getEmail().trim().toLowerCase());

        // Check duplicate email
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {

            response.put("message", "Email already exists");

            return ResponseEntity.badRequest().body(response);
        }

        // Encrypt password
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        // Set role
        admin.setRole("ADMIN");

        adminRepository.save(admin);

        response.put("message", "Admin created successfully");

        return ResponseEntity.ok(response);
    }

    // ADMIN DASHBOARD STATISTICS
    @GetMapping("/dashboard")
    public Map<String, Long> dashboard() {

        Map<String, Long> stats = new HashMap<>();

        stats.put("students", studentRepository.count());
        stats.put("companies", companyRepository.count());
        stats.put("jobs", jobRepository.count());
        stats.put("applications", applicationRepository.count());

        return stats;
    }

    // GET ALL STUDENTS
    @GetMapping("/students")
    public Object getAllStudents() {
        return studentRepository.findAll();
    }

    // GET ALL COMPANIES
    @GetMapping("/companies")
    public Object getAllCompanies() {
        return companyRepository.findAll();
    }

    // GET ALL JOBS
    @GetMapping("/jobs")
    public Object getAllJobs() {
        return jobRepository.findAll();
    }

    // GET ALL APPLICATIONS
    @GetMapping("/applications")
    public Object getAllApplications() {
        return applicationRepository.findAll();
    }
}
