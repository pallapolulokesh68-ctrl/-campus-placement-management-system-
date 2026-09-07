package com.compus.placementmanagement;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EligibilityService {

    public EligibilityResponse checkEligibility(Student student, Job job) {

        EligibilityResponse response = new EligibilityResponse();

        // Check CGPA
        boolean cgpaEligible =
                student.getCgpa() >= job.getRequiredCgpa();

        if (cgpaEligible) {
            response.setCgpaStatus("Eligible");
        } else {
            response.setCgpaStatus("Not Eligible");
        }

        // Student skills
        String studentSkills = student.getSkills().toLowerCase();

        // Required skills
        String[] requiredSkills =
                job.getRequiredSkills().split(",");

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        // Check every required skill
        for (String skill : requiredSkills) {

            String trimmedSkill = skill.trim();

            if (studentSkills.contains(trimmedSkill.toLowerCase())) {
                matchedSkills.add(trimmedSkill);
            } else {
                missingSkills.add(trimmedSkill);
            }
        }

        response.setMatchedSkills(matchedSkills);
        response.setMissingSkills(missingSkills);

        // Final eligibility
        boolean eligible =
                cgpaEligible && missingSkills.isEmpty();

        response.setEligible(eligible);

        return response;
    }
}