package com.compus.placementmanagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================
    // NAME
    // =========================

    @NotBlank(message = "Name is required")
    private String name;


    // =========================
    // EMAIL
    // =========================

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;


    // =========================
    // PASSWORD
    // =========================

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    // =========================
    // ROLE
    // =========================

    private String role;


    // =========================
    // DEPARTMENT
    // =========================

    @NotBlank(message = "Department is required")
    private String department;


    // =========================
    // CGPA
    // =========================

    @NotNull(message = "CGPA is required")
    @DecimalMin(
            value = "0.0",
            message = "CGPA cannot be less than 0"
    )
    @DecimalMax(
            value = "10.0",
            message = "CGPA cannot be greater than 10"
    )
    private Double cgpa;


    // =========================
    // SKILLS
    // =========================

    @NotBlank(message = "Skills are required")
    private String skills;


    // =========================
    // RESUME
    // =========================

    private String resume;


    // =========================
    // APPLICATIONS
    // =========================

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Application> applications;


    // =========================
    // GETTERS AND SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }


    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }


    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }


    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(
            List<Application> applications) {

        this.applications = applications;
    }
}