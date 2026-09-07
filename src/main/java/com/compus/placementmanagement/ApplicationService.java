package com.compus.placementmanagement;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final JobRepository jobRepository;
    private final EligibilityService eligibilityService;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            StudentRepository studentRepository,
            JobRepository jobRepository,
            EligibilityService eligibilityService) {

        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.jobRepository = jobRepository;
        this.eligibilityService = eligibilityService;
    }


    // =========================================================
    // APPLY FOR JOB
    // =========================================================

    public Application applyForJob(
            Long studentId,
            Long jobId) {

        Student student =
                studentRepository
                        .findById(studentId)
                        .orElse(null);

        if (student == null) {
            throw new RuntimeException(
                    "Student not found"
            );
        }


        Job job =
                jobRepository
                        .findById(jobId)
                        .orElse(null);

        if (job == null) {
            throw new RuntimeException(
                    "Job not found"
            );
        }


        // Check duplicate application

        boolean alreadyApplied =
                applicationRepository
                        .existsByStudentIdAndJobId(
                                studentId,
                                jobId
                        );

        if (alreadyApplied) {

            throw new RuntimeException(
                    "You have already applied for this job"
            );
        }


        // Check eligibility

        EligibilityResponse eligibility =
                eligibilityService.checkEligibility(
                        student,
                        job
                );

        if (!eligibility.isEligible()) {

            throw new RuntimeException(
                    "You are not eligible for this job"
            );
        }


        // Create application

        Application application =
                new Application();

        application.setStudent(student);
        application.setJob(job);
        application.setStatus("Applied");
        application.setAppliedDate(
                LocalDate.now()
        );


        return applicationRepository.save(
                application
        );
    }


    // =========================================================
    // OLD APPLY METHOD
    // =========================================================
    // Kept so existing backend code does not break.

    public Application applyForJob(
            Application application) {

        if (application == null) {

            throw new RuntimeException(
                    "Application data is required"
            );
        }


        if (application.getStudent() == null) {

            throw new RuntimeException(
                    "Student information is required"
            );
        }


        if (application.getStudent().getId() == null) {

            throw new RuntimeException(
                    "Student ID is required"
            );
        }


        if (application.getJob() == null) {

            throw new RuntimeException(
                    "Job information is required"
            );
        }


        if (application.getJob().getId() == null) {

            throw new RuntimeException(
                    "Job ID is required"
            );
        }


        return applyForJob(
                application.getStudent().getId(),
                application.getJob().getId()
        );
    }


    // =========================================================
    // GET ALL APPLICATIONS
    // =========================================================

    public List<Application> getAllApplications() {

        return applicationRepository.findAll();
    }


    // =========================================================
    // GET APPLICATION BY ID
    // =========================================================

    public Application getApplication(Long id) {

        return applicationRepository
                .findById(id)
                .orElse(null);
    }


    // =========================================================
    // GET APPLICATIONS BY STUDENT
    // =========================================================

    public List<Application> getApplicationsByStudent(
            Long studentId) {

        return applicationRepository
                .findByStudentId(studentId);
    }


    // =========================================================
    // GET APPLICATIONS BY JOB
    // =========================================================

    public List<Application> getApplicationsByJob(
            Long jobId) {

        return applicationRepository
                .findByJobId(jobId);
    }


    // =========================================================
    // GET APPLICATIONS FOR COMPANY JOB
    // =========================================================

    public List<Application> getApplicationsByJobForCompany(
            Long jobId,
            Long companyId) {

        Job job =
                jobRepository
                        .findById(jobId)
                        .orElse(null);

        if (job == null) {

            throw new RuntimeException(
                    "Job not found"
            );
        }


        if (
                job.getCompany() == null ||
                        !job.getCompany()
                                .getId()
                                .equals(companyId)
        ) {

            throw new RuntimeException(
                    "You can only view applicants for your own jobs"
            );
        }


        return applicationRepository
                .findByJobId(jobId);
    }


    // =========================================================
    // UPDATE APPLICATION
    // =========================================================

    public Application updateApplication(
            Long id,
            Application application,
            Long companyId) {

        Application existingApplication =
                applicationRepository
                        .findById(id)
                        .orElse(null);

        if (existingApplication == null) {
            return null;
        }


        Job job =
                existingApplication.getJob();

        if (job == null) {

            throw new RuntimeException(
                    "Job not found"
            );
        }


        if (
                job.getCompany() == null ||
                        !job.getCompany()
                                .getId()
                                .equals(companyId)
        ) {

            throw new RuntimeException(
                    "You can only update applications for your own jobs"
            );
        }


        if (
                application == null ||
                        application.getStatus() == null ||
                        application.getStatus().trim().isEmpty()
        ) {

            throw new RuntimeException(
                    "Application status is required"
            );
        }


        existingApplication.setStatus(
                application.getStatus()
        );


        return applicationRepository.save(
                existingApplication
        );
    }


    // =========================================================
    // DELETE APPLICATION
    // =========================================================

    public String deleteApplication(Long id) {

        Application application =
                applicationRepository
                        .findById(id)
                        .orElse(null);

        if (application == null) {

            return "Application not found";
        }


        applicationRepository.deleteById(id);

        return "Application deleted successfully";
    }
}