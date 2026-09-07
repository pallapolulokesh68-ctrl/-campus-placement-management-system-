package com.compus.placementmanagement;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;

    public JobService(
            JobRepository jobRepository,
            ApplicationRepository applicationRepository,
            CompanyRepository companyRepository) {

        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.companyRepository = companyRepository;
    }


    // CREATE JOB
    public Job addJob(Job job, Long companyId) {

        Company company =
                companyRepository.findById(companyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Company not found"
                                ));

        job.setCompany(company);

        return jobRepository.save(job);
    }


    // GET ALL JOBS
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }


    // GET JOB BY ID
    public Job getJob(Long id) {
        return jobRepository
                .findById(id)
                .orElse(null);
    }


    // GET COMPANY JOBS
    public List<Job> getJobsByCompany(Long companyId) {
        return jobRepository.findByCompanyId(companyId);
    }


    // UPDATE JOB
    public Job updateJob(
            Long id,
            Job job,
            Long companyId) {

        Job existingJob =
                jobRepository.findById(id)
                        .orElse(null);

        if (existingJob == null) {
            return null;
        }


        if (existingJob.getCompany() == null ||
                !existingJob.getCompany()
                        .getId()
                        .equals(companyId)) {

            throw new RuntimeException(
                    "You can only update your own jobs"
            );
        }


        existingJob.setJobTitle(
                job.getJobTitle()
        );

        existingJob.setDescription(
                job.getDescription()
        );

        existingJob.setRequiredCgpa(
                job.getRequiredCgpa()
        );

        existingJob.setRequiredSkills(
                job.getRequiredSkills()
        );

        existingJob.setSalary(
                job.getSalary()
        );


        return jobRepository.save(
                existingJob
        );
    }


    // DELETE JOB
    public String deleteJob(
            Long id,
            Long companyId) {

        Job existingJob =
                jobRepository.findById(id)
                        .orElse(null);

        if (existingJob == null) {
            return "Job not found";
        }


        if (existingJob.getCompany() == null ||
                !existingJob.getCompany()
                        .getId()
                        .equals(companyId)) {

            throw new RuntimeException(
                    "You can only delete your own jobs"
            );
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
}