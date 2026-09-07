package com.compus.placementmanagement;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder) {

        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // =========================
    // ADD STUDENT
    // =========================

    public Student addStudent(Student student) {

        if (student.getPassword() != null &&
                !student.getPassword().isBlank()) {

            student.setPassword(
                    passwordEncoder.encode(student.getPassword())
            );
        }

        return studentRepository.save(student);
    }


    // =========================
    // GET ALL STUDENTS
    // =========================

    public List<Student> getAllStudents() {

        return studentRepository.findAll();
    }


    // =========================
    // GET STUDENT BY ID
    // =========================

    public Student getStudent(Long id) {

        return studentRepository.findById(id)
                .orElse(null);
    }


    // =========================
    // UPDATE STUDENT
    // =========================

    public Student updateStudent(Long id, Student student) {

        Student existingStudent =
                studentRepository.findById(id)
                        .orElse(null);

        if (existingStudent == null) {
            return null;
        }

        existingStudent.setName(student.getName());

        existingStudent.setEmail(student.getEmail());

        existingStudent.setDepartment(
                student.getDepartment()
        );

        existingStudent.setCgpa(
                student.getCgpa()
        );

        existingStudent.setSkills(
                student.getSkills()
        );

        existingStudent.setResume(
                student.getResume()
        );


        // =========================
        // UPDATE PASSWORD
        // =========================

        if (student.getPassword() != null &&
                !student.getPassword().isBlank()) {

            existingStudent.setPassword(
                    passwordEncoder.encode(
                            student.getPassword()
                    )
            );
        }

        return studentRepository.save(existingStudent);
    }


    // =========================
    // DELETE STUDENT
    // =========================

    public String deleteStudent(Long id) {

        studentRepository.deleteById(id);

        return "Student deleted successfully";
    }
}