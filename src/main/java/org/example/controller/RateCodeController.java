package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.example.model.CodeResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rate")
public class RateCodeController {
    private final String apiKey = "12sk-pPQFkd4n0M18ao8Uw2dLT3BlbkFJC624vqg1LIu8g8uXpBwO";
    private final String apiUrl = "https://api.openai.com/v1/chat/completions";
    private final String model = "gpt-3.5-turbo";

    @PostMapping("/rate-code")
    public List<CodeResponse> rateCode(@RequestBody Map<String, String> request) {
        String problemStatement = request.get("problemStatement");
        String code = request.get("code");
        String response = generateRatingForCode(code, problemStatement);
        response = response.replaceAll("\\\\n", "\n");
        CodeResponse code1= new CodeResponse();
        code1.setCodeResponse(response);
        List<CodeResponse> lst= new ArrayList<>();
        lst.add(code1);
        return lst;
        //return "Rating for the provided code: " + rating;
    }

    private String generateRatingForCode(String code, String problemStatement) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonBody = objectMapper.createObjectNode();

            String prompt = "Evaluate the following code in response to the problem statement provided:\n\n" +
                    "Problem Statement: " + problemStatement + "\n\n" +
                    "Code: " + code + "\n\n" +
                    "1. Rate this code as per the JAVA standards.\n\n"+
                    "2. Does the provided code compile successfully?\n\n" +
                    "3. Please generate an expected output based on the problem statement.\n\n" +
                    "4. Please generate the actual output produced by the provided code.\n\n" +
                    "5. Does the code output match the expected output described in the problem statement?\n\n" +
                    "6. Are coding standards and best practices followed in the provided code?\n\n" +
                    "7. Calculate the exact percentage of completion of the code out of 100?\n\n" +
                    "8. Rate the code on a scale of 1 to 5 based on the accuracy of the output (1 being the lowest, 5 being the highest)?\n";

            ObjectNode messagesNode = objectMapper.createObjectNode();
            messagesNode.put("role", "user");
            messagesNode.put("content", prompt);
            jsonBody.put("model", model);
            jsonBody.putArray("messages").add(messagesNode);

            OutputStream os = connection.getOutputStream();
            byte[] input = objectMapper.writeValueAsBytes(jsonBody);
            os.write(input, 0, input.length);

            int responseCode = connection.getResponseCode();

            if (responseCode == 429) {
                // Handle rate limit exceeded or other errors
                return "Rate limit exceeded or other error.";
            }

            String response = readResponse(connection);
            String rating = extractRatingFromJSONResponse(response);
            return rating;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    private String extractRatingFromJSONResponse(String response) {
        int start = response.indexOf("content") + 11;
        int end = response.indexOf("\"", start);
        return response.substring(start, end);
    }
}