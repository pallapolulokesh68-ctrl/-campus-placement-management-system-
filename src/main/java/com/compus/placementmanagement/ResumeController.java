package com.compus.placementmanagement;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    private final StudentRepository studentRepository;
    private final ApplicationRepository applicationRepository;

    private final String uploadDirectory =
            "uploads/resumes/";

    public ResumeController(
            StudentRepository studentRepository,
            ApplicationRepository applicationRepository) {

        this.studentRepository =
                studentRepository;

        this.applicationRepository =
                applicationRepository;
    }


    // =========================
    // UPLOAD RESUME
    // =========================

    @PostMapping("/upload/{studentId}")
    public ResponseEntity<?> uploadResume(
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file) {

        try {

            Student student =
                    studentRepository
                            .findById(studentId)
                            .orElse(null);

            if (student == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Student not found");
            }


            if (file.isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Please select a file");
            }


            String fileName =
                    file.getOriginalFilename();


            if (fileName == null ||
                    !fileName
                            .toLowerCase()
                            .endsWith(".pdf")) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Only PDF files are allowed"
                        );
            }


            Path uploadPath =
                    Paths.get(uploadDirectory);

            Files.createDirectories(
                    uploadPath
            );


            String safeFileName =
                    Paths.get(fileName)
                            .getFileName()
                            .toString();


            String uniqueFileName =
                    studentId + "_" +
                            safeFileName;


            Path filePath =
                    uploadPath.resolve(
                            uniqueFileName
                    ).normalize();


            if (!filePath.startsWith(
                    uploadPath.toAbsolutePath()
            )) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Invalid file name"
                        );
            }


            Files.write(
                    filePath,
                    file.getBytes()
            );


            student.setResume(
                    uniqueFileName
            );


            studentRepository.save(
                    student
            );


            return ResponseEntity.ok(
                    "Resume uploaded successfully"
            );

        } catch (IOException e) {

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Failed to upload resume"
                    );
        }
    }


    // =========================
    // VIEW / DOWNLOAD RESUME
    // =========================

    @GetMapping("/view/{studentId}")
    public ResponseEntity<?> viewResume(
            @PathVariable Long studentId,
            Authentication authentication) {

        try {

            Long companyId =
                    (Long) authentication.getPrincipal();


            Student student =
                    studentRepository
                            .findById(studentId)
                            .orElse(null);


            if (student == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Student not found"
                        );
            }


            if (student.getResume() == null ||
                    student.getResume().isBlank()) {

                return ResponseEntity
                        .notFound()
                        .build();
            }


            // Check whether this student
            // applied for a job belonging
            // to the logged-in company.

            List<Application> applications =
                    applicationRepository
                            .findByStudentId(studentId);


            boolean allowed = false;


            for (Application application :
                    applications) {

                if (application.getJob() == null ||
                        application.getJob()
                                .getCompany() == null) {

                    continue;
                }


                Long applicationCompanyId =
                        application
                                .getJob()
                                .getCompany()
                                .getId();


                if (applicationCompanyId
                        .equals(companyId)) {

                    allowed = true;
                    break;
                }
            }


            if (!allowed) {

                return ResponseEntity
                        .status(403)
                        .body(
                                "You can only view resumes of students who applied to your jobs"
                        );
            }


            Path uploadPath =
                    Paths.get(
                                    uploadDirectory
                            )
                            .toAbsolutePath()
                            .normalize();


            Path filePath =
                    uploadPath
                            .resolve(
                                    student.getResume()
                            )
                            .normalize();


            if (!filePath.startsWith(
                    uploadPath
            )) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Invalid resume path"
                        );
            }


            if (!Files.exists(filePath) ||
                    !Files.isRegularFile(filePath)) {

                return ResponseEntity
                        .notFound()
                        .build();
            }


            Resource resource =
                    new UrlResource(
                            filePath
                                    .toUri()
                    );


            return ResponseEntity.ok()

                    .contentType(
                            MediaType.APPLICATION_PDF
                    )

                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" +
                                    student.getResume() +
                                    "\""
                    )

                    .body(resource);


        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Unable to open resume"
                    );
        }
    }
}