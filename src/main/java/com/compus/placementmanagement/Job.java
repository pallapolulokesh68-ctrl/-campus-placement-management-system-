package com.compus.placementmanagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobTitle;

    private String description;

    private Double requiredCgpa;

    private String requiredSkills;

    private Double salary;


    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


    @OneToMany(mappedBy = "job")
    @JsonIgnore
    private List<Application> applications;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Double getRequiredCgpa() {
        return requiredCgpa;
    }

    public void setRequiredCgpa(Double requiredCgpa) {
        this.requiredCgpa = requiredCgpa;
    }


    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }


    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }


    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }


    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(
            List<Application> applications) {
        this.applications = applications;
    }
}