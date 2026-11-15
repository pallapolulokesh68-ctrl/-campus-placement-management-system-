
package com.compus.placementmanagement.controller;

import com.compus.placementmanagement.StudentRepository;
import com.compus.placementmanagement.CompanyRepository;
import com.compus.placementmanagement.JobRepository;
import com.compus.placementmanagement.ApplicationRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    public AdminController(
            StudentRepository studentRepository,
            CompanyRepository companyRepository,
            JobRepository jobRepository,
            ApplicationRepository applicationRepository) {

        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
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
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    // GET ALL COMPANIES
    @GetMapping("/companies")
    public ResponseEntity<?> getAllCompanies() {
        return ResponseEntity.ok(companyRepository.findAll());
    }

    // GET ALL JOBS
    @GetMapping("/jobs")
    public ResponseEntity<?> getAllJobs() {
        return ResponseEntity.ok(jobRepository.findAll());
    }

    // GET ALL APPLICATIONS
    @GetMapping("/applications")
    public ResponseEntity<?> getAllApplications() {
        return ResponseEntity.ok(applicationRepository.findAll());
    }
}
