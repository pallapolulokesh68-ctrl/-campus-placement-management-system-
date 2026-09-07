package com.compus.placementmanagement;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eligibility")
public class EligibilityController {

    private final EligibilityService eligibilityService;
    private final StudentRepository studentRepository;
    private final JobRepository jobRepository;

    public EligibilityController(
            EligibilityService eligibilityService,
            StudentRepository studentRepository,
            JobRepository jobRepository) {

        this.eligibilityService = eligibilityService;
        this.studentRepository = studentRepository;
        this.jobRepository = jobRepository;
    }

    @GetMapping("/{studentId}/{jobId}")
    public EligibilityResponse checkEligibility(
            @PathVariable Long studentId,
            @PathVariable Long jobId) {

        Student student = studentRepository
                .findById(studentId)
                .orElse(null);

        Job job = jobRepository
                .findById(jobId)
                .orElse(null);

        if (student == null) {
            throw new RuntimeException("Student not found");
        }

        if (job == null) {
            throw new RuntimeException("Job not found");
        }

        return eligibilityService.checkEligibility(student, job);
    }
}