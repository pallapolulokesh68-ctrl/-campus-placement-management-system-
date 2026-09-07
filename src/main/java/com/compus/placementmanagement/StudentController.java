package com.compus.placementmanagement;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentController(
            StudentService studentService,
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {

        this.studentService = studentService;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public Student registerStudent(
            @Valid @RequestBody Student student) {

        student.setRole("STUDENT");

        return studentService.addStudent(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(
            @PathVariable Long id) {

        return studentService.getStudent(id);
    }

    @PutMapping("/{id}")
    public Student updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student student,
            Authentication authentication) {

        Long loggedInStudentId =
                (Long) authentication.getPrincipal();

        if (!loggedInStudentId.equals(id)) {
            throw new RuntimeException(
                    "You can only update your own profile"
            );
        }

        return studentService.updateStudent(
                id,
                student
        );
    }

    @PutMapping("/{id}/password")
    public Map<String, String> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        Long loggedInStudentId =
                (Long) authentication.getPrincipal();

        if (!loggedInStudentId.equals(id)) {
            throw new RuntimeException(
                    "You can only change your own password"
            );
        }

        Student student =
                studentRepository
                        .findById(id)
                        .orElse(null);

        if (student == null) {
            throw new RuntimeException(
                    "Student not found"
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

        if (newPassword.length() < 6) {

            throw new RuntimeException(
                    "New password must contain at least 6 characters"
            );
        }

        if (!passwordEncoder.matches(
                currentPassword,
                student.getPassword())) {

            throw new RuntimeException(
                    "Current password is incorrect"
            );
        }

        student.setPassword(
                passwordEncoder.encode(
                        newPassword
                )
        );

        studentRepository.save(student);

        return Map.of(
                "message",
                "Password changed successfully"
        );
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(
            @PathVariable Long id,
            Authentication authentication) {

        Long loggedInStudentId =
                (Long) authentication.getPrincipal();

        if (!loggedInStudentId.equals(id)) {
            throw new RuntimeException(
                    "You can only delete your own account"
            );
        }

        return studentService.deleteStudent(id);
    }
}