package com.compus.placementmanagement;

import java.util.List;

public class EligibilityResponse {

    private boolean eligible;
    private String cgpaStatus;
    private List<String> matchedSkills;
    private List<String> missingSkills;


    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public String getCgpaStatus() {
        return cgpaStatus;
    }

    public void setCgpaStatus(String cgpaStatus) {
        this.cgpaStatus = cgpaStatus;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }
}