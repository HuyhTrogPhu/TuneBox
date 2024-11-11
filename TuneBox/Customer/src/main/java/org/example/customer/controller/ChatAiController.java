package org.example.customer.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Value;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/customer/chatAI")
public class ChatAiController {



    @PostMapping("/chat")
    public ResponseEntity<String> generateTrack() {
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpPost request = new HttpPost("https://api.openai.com/v1/chat/completions");
//            request.setHeader("Authorization", "Bearer " + openaiApiKey);
//            request.setHeader("Content-Type", "application/json");
//
//            // Chuẩn bị dữ liệu JSON cho yêu cầu API
//            JSONObject systemMessage = new JSONObject();
//            systemMessage.put("role", "system");
//            systemMessage.put("content", "You are a helpful assistant.");
//
//            JSONObject userMessage = new JSONObject();
//            userMessage.put("role", "user");
//            userMessage.put("content", userInput);
//
//            JSONArray messagesArray = new JSONArray();
//            messagesArray.add(systemMessage);
//            messagesArray.add(userMessage);
//
//            JSONObject data = new JSONObject();
//            data.put("model", "gpt-3.5-turbo");
//            data.put("messages", messagesArray);
//
//            request.setEntity(new StringEntity(data.toString()));
//
//            try (CloseableHttpResponse response = httpClient.execute(request)) {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//                StringBuilder result = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    result.append(line);
//                }
//
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode jsonResponse = mapper.readTree(result.toString());
//
//                System.out.println("API Response: " + jsonResponse);  // In ra toàn bộ phản hồi từ OpenAI
//
//
//                // Kiểm tra xem trường "choices" có tồn tại và có dữ liệu không
//                JsonNode choicesNode = jsonResponse.get("choices");
//                if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
//                    JsonNode firstChoice = choicesNode.get(0);
//
//                    // Kiểm tra xem trường "message" và "content" có tồn tại không
//                    JsonNode messageNode = firstChoice.get("message");
//                    if (messageNode != null) {
//                        JsonNode contentNode = messageNode.get("content");
//                        if (contentNode != null) {
//                            String messageContent = contentNode.asText();
//                            return ResponseEntity.ok(messageContent);
//                        } else {
//                            return ResponseEntity.status(500).body("Content is missing in the message.");
//                        }
//                    } else {
//                        return ResponseEntity.status(500).body("Message is missing in the response.");
//                    }
//                } else {
//                    return ResponseEntity.status(500).body("Choices are missing or empty.");
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Error calling OpenAI API");
//        }
        return ResponseEntity.status(500).body("Error calling OpenAI API");
    }
}
