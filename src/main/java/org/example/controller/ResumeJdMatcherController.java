package org.example.controller;

import org.apache.commons.lang3.StringUtils;
import org.example.configuration.DatabaseConnection;
import org.example.model.Request;
import org.example.model.ResumeDetailsDTO;
import org.example.model.ResumeMatchDTO;
import org.example.service.ResumeJdMatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ResumeJdMatcherController {

    @Autowired
    private ResumeJdMatcherService resumeJdMatcherService;
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/ResumeMatch")
    public List<ResumeMatchDTO> readDocument(@RequestParam("documentPath") String documentPath) throws Exception {
        //String documentPath = request;
        String jDPath = documentPath + "\\JD";
        File jdDirectoryPath = new File(jDPath);
        File filesList[] = jdDirectoryPath.listFiles();
        if (filesList.length > 1) {
            throw new Exception("Sorry CUES, This Folder contains more than one JD");
        } else {
            for (File file : filesList) {
                jDPath = file.toString();

            }

        }
        Set<String> jDKeyWords = resumeJdMatcherService.readJD(jDPath);
        System.out.println("JD Skill Sets:" + jDKeyWords);
        String resumePath = documentPath + "\\RESUMES";
        File resumeDirectoryPath = new File(resumePath);
        File resumeFilesList[] = resumeDirectoryPath.listFiles();
        List<ResumeDetailsDTO> resumeList = new ArrayList<ResumeDetailsDTO>();
        if (resumeFilesList.length < 1) {
            throw new Exception("Sorry CUES, No Resume Found in this Path");
        } else {
            for (File file : resumeFilesList) {

                String resumeName = file.toString();
                String resume = resumeJdMatcherService.readResume(resumeName);
                String skillSet = StringUtils.substringBetween(resumeName, "Primary skills", "Operating System");

                ResumeDetailsDTO resumeDetails = new ResumeDetailsDTO();
                resumeDetails.setResumeName(resumeName);
                resumeDetails.setResumeDetails(resume);
                resumeList.add(resumeDetails);
            }
        }
        List<ResumeMatchDTO> resumeMatch = resumeJdMatcherService.analyzeResume(jDKeyWords, resumeList);
        insertData(resumeMatch);
        return resumeMatch;
    }

    public void insertData(List<ResumeMatchDTO> resumeMatch) throws Exception {
        for (ResumeMatchDTO match : resumeMatch) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                if (connection != null) {
                    String insertQuery = "INSERT INTO skills_table (resumeName, skills, percentage) VALUES (?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, match.getResumeName());
                        preparedStatement.setString(2, String.join(", ", match.getMatchedKeywords())); // Convert list to comma-separated string
                        preparedStatement.setInt(3, match.getPercentageMatch());
                        preparedStatement.executeUpdate();
                    }
                } else {
                    throw new Exception("Failed to establish a database connection.");
                }
            } catch (SQLException e) {
                // Handle any database-related exceptions here
                e.printStackTrace();
                throw new Exception("Error while inserting data into the database.");
            }
        }
    }
}