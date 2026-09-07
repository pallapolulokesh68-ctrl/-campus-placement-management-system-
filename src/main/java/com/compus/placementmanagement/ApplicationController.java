package com.compus.placementmanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(
            ApplicationService applicationService) {

        this.applicationService = applicationService;
    }


    // =========================================================
    // APPLY FOR JOB - STUDENT
    // =========================================================

    @PostMapping
    public ResponseEntity<?> applyForJob(
            @RequestBody Map<String, Object> request) {

        try {

            if (request.get("studentId") == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Student ID is required"
                                )
                        );
            }

            if (request.get("jobId") == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Job ID is required"
                                )
                        );
            }


            Long studentId =
                    Long.valueOf(
                            request
                                    .get("studentId")
                                    .toString()
                    );

            Long jobId =
                    Long.valueOf(
                            request
                                    .get("jobId")
                                    .toString()
                    );


            Application application =
                    applicationService.applyForJob(
                            studentId,
                            jobId
                    );


            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Application submitted successfully",
                            "applicationId",
                            application.getId()
                    )
            );


        } catch (Exception error) {

            error.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    error.getMessage() != null
                                            ? error.getMessage()
                                            : "Unable to apply for job"
                            )
                    );
        }
    }


    // =========================================================
    // GET ALL APPLICATIONS - ADMIN
    // =========================================================

    @GetMapping
    public List<Application> getAllApplications() {

        return applicationService
                .getAllApplications();
    }


    // =========================================================
    // GET APPLICATION BY ID - ADMIN
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplication(
            @PathVariable Long id) {

        Application application =
                applicationService
                        .getApplication(id);

        if (application == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(application);
    }


    // =========================================================
    // GET STUDENT APPLICATIONS
    // =========================================================

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentApplications(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                applicationService
                        .getApplicationsByStudent(
                                studentId
                        )
        );
    }


    // =========================================================
    // GET JOB APPLICANTS - COMPANY
    // =========================================================

    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getJobApplicants(
            @PathVariable Long jobId,
            Authentication authentication) {

        try {

            Long companyId =
                    Long.valueOf(
                            authentication.getName()
                    );


            return ResponseEntity.ok(
                    applicationService
                            .getApplicationsByJobForCompany(
                                    jobId,
                                    companyId
                            )
            );


        } catch (Exception error) {

            error.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    error.getMessage() != null
                                            ? error.getMessage()
                                            : "Unable to load applicants"
                            )
                    );
        }
    }


    // =========================================================
    // UPDATE APPLICATION STATUS - COMPANY
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        try {

            String status =
                    request.get("status");


            if (status == null ||
                    status.trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Application status is required"
                                )
                        );
            }


            Long companyId =
                    Long.valueOf(
                            authentication.getName()
                    );


            Application application =
                    new Application();

            application.setStatus(
                    status
            );


            Application updatedApplication =
                    applicationService.updateApplication(
                            id,
                            application,
                            companyId
                    );


            if (updatedApplication == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Application not found"
                                )
                        );
            }


            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Application status updated successfully",
                            "status",
                            updatedApplication.getStatus()
                    )
            );


        } catch (Exception error) {

            error.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    error.getMessage() != null
                                            ? error.getMessage()
                                            : "Unable to update application"
                            )
                    );
        }
    }


    // =========================================================
    // DELETE APPLICATION - ADMIN
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(
            @PathVariable Long id) {

        try {

            String result =
                    applicationService
                            .deleteApplication(id);


            if (!result.equals(
                    "Application deleted successfully")) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        result
                                )
                        );
            }


            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            result
                    )
            );


        } catch (Exception error) {

            error.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    error.getMessage() != null
                                            ? error.getMessage()
                                            : "Unable to delete application"
                            )
                    );
        }
    }
}