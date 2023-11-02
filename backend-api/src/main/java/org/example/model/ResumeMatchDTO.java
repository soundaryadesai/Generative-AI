package org.example.model;


import java.util.List;


public class ResumeMatchDTO {
    private String resumeName;

    private int percentage;

    private List<String> skills;
    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    public int getPercentageMatch() {
        return percentage;
    }

    public void setPercentageMatch(int percentageMatch) {
        this.percentage = percentageMatch;
    }

    public List<String> getMatchedKeywords() {
        return skills;
    }

    public void setMatchedKeywords(List<String> matchedKeywords) {
        this.skills = matchedKeywords;
    }

    @Override
    public String toString() {
        return "resumeName= " + resumeName + ", percentageMatch= " + percentage
                + ", matchedKeywords= " + skills + "\n";
    }

}