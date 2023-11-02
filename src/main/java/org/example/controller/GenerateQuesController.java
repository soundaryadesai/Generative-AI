package org.example.controller;

import org.example.configuration.DatabaseConnection;
import org.example.model.Question;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/compare")
public class GenerateQuesController {

    @GetMapping("/skills")
    public List<String> getSkillsFromResume(@RequestParam("resumeName") String resumeName) {
        return fetchSkillsFromDatabase(resumeName);
    }

    @GetMapping("/generateQuestion")
    public List<Question> generateCodingQuestion(@RequestParam("resumeName") String resumeName) {
        List<String> skills = fetchSkillsFromDatabase(resumeName);
        System.out.println("Skills:" +skills);
        StringBuilder codingQuestions = new StringBuilder();
         List<Question> questionList= new ArrayList<Question>();

        
        for (String skill : skills) {
            String[] individualSkills = skill.split(", ");
            for (String individualSkill : individualSkills) {
            	Question q = new Question();
                String codingQuestion = openAiCodingQuestion(individualSkill);

            	q.setSkill(individualSkill);
            	q.setQuestions(codingQuestion);
            	questionList.add(q);
                //codingQuestions.append("Skill: ").append(individualSkill);
                //codingQuestions.append("Coding Question: ").append(codingQuestion);
            }
        }
        
       // question.setQuestions(codingQuestions.toString());
      //  question.setQuestions(codingQuestions.toString());
        return questionList;
    }
    public List<String> fetchSkillsFromDatabase(String resumeName) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT skills FROM skills_table WHERE resumename = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, resumeName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String[] skillsArray = new String[]{resultSet.getString("skills")};
                        return Arrays.asList(skillsArray);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch skills from the database.");
        }
        return Collections.emptyList();
    }
    public String openAiCodingQuestion(String skill) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "12sk-pPQFkd4n0M18ao8Uw2dLT3BlbkFJC624vqg1LIu8g8uXpBwO";
        String model = "gpt-3.5-turbo";
        int retryCount = 0;
        int maxRetries = 3;

        int initialRetryDelayMillis = 1000; 
        int maxRetryDelayMillis = 60000;

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            String prompt = "Generate a coding question related to a " + skill + " programming";
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            while (retryCount < maxRetries) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                    br.close();

                    return extractMessageFromJSONResponse(response.toString());
                } catch (IOException e) {
                    if (connection.getResponseCode() == 429) {
                        int retryDelayMillis = Math.min(initialRetryDelayMillis * (1 << retryCount), maxRetryDelayMillis);
                        Thread.sleep(retryDelayMillis);
                        retryCount++;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throw new RuntimeException("Maximum retries reached without success.");
    }

    public String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;
        int end = response.indexOf("\"", start);
        return response.substring(start, end);
    }
}