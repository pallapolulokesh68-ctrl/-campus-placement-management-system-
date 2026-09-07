package com.compus.placementmanagement;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public AdminController(
            AdminRepository adminRepository,
            StudentRepository studentRepository,
            CompanyRepository companyRepository,
            JobRepository jobRepository,
            ApplicationRepository applicationRepository) {

        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }


    // ================= DASHBOARD =================

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {

        Map<String, Object> dashboard =
                new HashMap<>();

        dashboard.put(
                "totalStudents",
                studentRepository.count()
        );

        dashboard.put(
                "totalCompanies",
                companyRepository.count()
        );

        dashboard.put(
                "totalJobs",
                jobRepository.count()
        );

        dashboard.put(
                "totalApplications",
                applicationRepository.count()
        );

        return dashboard;
    }


    // ================= STATISTICS =================

    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {

        Map<String, Object> statistics =
                new HashMap<>();

        long totalStudents =
                studentRepository.count();

        long totalApplications =
                applicationRepository.count();


        long shortlisted =
                applicationRepository
                        .findAll()
                        .stream()
                        .filter(application ->
                                "Shortlisted".equalsIgnoreCase(
                                        application.getStatus()
                                )
                        )
                        .count();


        long interview =
                applicationRepository
                        .findAll()
                        .stream()
                        .filter(application ->
                                "Interview".equalsIgnoreCase(
                                        application.getStatus()
                                )
                        )
                        .count();


        long selected =
                applicationRepository
                        .findAll()
                        .stream()
                        .filter(application ->
                                "Selected".equalsIgnoreCase(
                                        application.getStatus()
                                )
                        )
                        .count();


        long rejected =
                applicationRepository
                        .findAll()
                        .stream()
                        .filter(application ->
                                "Rejected".equalsIgnoreCase(
                                        application.getStatus()
                                )
                        )
                        .count();


        // ================= PLACEMENT PERCENTAGE =================

        double placementPercentage = 0.0;

        if (totalStudents > 0) {

            placementPercentage =
                    ((double) selected /
                            totalStudents) * 100;
        }


        statistics.put(
                "totalStudents",
                totalStudents
        );

        statistics.put(
                "totalCompanies",
                companyRepository.count()
        );

        statistics.put(
                "totalJobs",
                jobRepository.count()
        );

        statistics.put(
                "totalApplications",
                totalApplications
        );

        statistics.put(
                "shortlistedApplications",
                shortlisted
        );

        statistics.put(
                "interviewApplications",
                interview
        );

        statistics.put(
                "selectedApplications",
                selected
        );

        statistics.put(
                "rejectedApplications",
                rejected
        );

        statistics.put(
                "placementPercentage",
                Math.round(
                        placementPercentage * 100.0
                ) / 100.0
        );


        return statistics;
    }


    // ================= STUDENTS =================

    @GetMapping("/students")
    public List<Student> getAllStudents() {

        return studentRepository.findAll();
    }


    @DeleteMapping("/students/{id}")
    public String deleteStudent(
            @PathVariable Long id) {

        Student student =
                studentRepository.findById(id)
                        .orElse(null);

        if (student == null) {
            return "Student not found";
        }

        long applicationCount =
                applicationRepository
                        .countByStudentId(id);

        if (applicationCount > 0) {

            return "Cannot delete student because the student has existing applications";
        }

        studentRepository.deleteById(id);

        return "Student deleted successfully";
    }


    // ================= COMPANIES =================

    @GetMapping("/companies")
    public List<Company> getAllCompanies() {

        return companyRepository.findAll();
    }


    @DeleteMapping("/companies/{id}")
    public String deleteCompany(
            @PathVariable Long id) {

        Company company =
                companyRepository.findById(id)
                        .orElse(null);

        if (company == null) {
            return "Company not found";
        }

        long jobCount =
                jobRepository.countByCompanyId(id);

        if (jobCount > 0) {

            return "Cannot delete company because the company has existing jobs";
        }

        companyRepository.deleteById(id);

        return "Company deleted successfully";
    }


    // ================= JOBS =================

    @GetMapping("/jobs")
    public List<Job> getAllJobs() {

        return jobRepository.findAll();
    }


    @DeleteMapping("/jobs/{id}")
    public String deleteJob(
            @PathVariable Long id) {

        Job job =
                jobRepository.findById(id)
                        .orElse(null);

        if (job == null) {
            return "Job not found";
        }

        long applicationCount =
                applicationRepository
                        .countByJobId(id);

        if (applicationCount > 0) {

            return "Cannot delete job because students have already applied";
        }

        jobRepository.deleteById(id);

        return "Job deleted successfully";
    }


    // ================= APPLICATIONS =================

    @GetMapping("/applications")
    public List<Application> getAllApplications() {

        return applicationRepository.findAll();
    }
}