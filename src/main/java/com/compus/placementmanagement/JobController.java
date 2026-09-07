package com.compus.placementmanagement;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;


    public JobController(JobService jobService) {
        this.jobService = jobService;
    }


    // CREATE JOB
    @PostMapping
    public Job addJob(
            @RequestBody Job job,
            Authentication authentication) {

        Long loggedInCompanyId =
                (Long) authentication.getPrincipal();

        return jobService.addJob(
                job,
                loggedInCompanyId
        );
    }


    // GET ALL JOBS
    @GetMapping
    public List<Job> getJobs() {

        return jobService.getAllJobs();
    }


    // GET COMPANY JOBS
    @GetMapping("/company/{companyId}")
    public List<Job> getJobsByCompany(
            @PathVariable Long companyId,
            Authentication authentication) {

        Long loggedInCompanyId =
                (Long) authentication.getPrincipal();


        if (!loggedInCompanyId.equals(companyId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only view your own jobs"
            );
        }


        return jobService.getJobsByCompany(
                companyId
        );
    }


    // GET JOB BY ID
    @GetMapping("/{id}")
    public Job getJob(
            @PathVariable Long id) {

        return jobService.getJob(id);
    }


    // UPDATE JOB
    @PutMapping("/{id}")
    public Job updateJob(
            @PathVariable Long id,
            @RequestBody Job job,
            Authentication authentication) {

        Long loggedInCompanyId =
                (Long) authentication.getPrincipal();


        return jobService.updateJob(
                id,
                job,
                loggedInCompanyId
        );
    }


    // DELETE JOB
    @DeleteMapping("/{id}")
    public String deleteJob(
            @PathVariable Long id,
            Authentication authentication) {

        Long loggedInCompanyId =
                (Long) authentication.getPrincipal();


        return jobService.deleteJob(
                id,
                loggedInCompanyId
        );
    }
}