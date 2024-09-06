package com.greensphere.greenspherewastecollectionservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greensphere.greenspherewastecollectionservice.service.WasteDataService;
import com.greensphere.greenspherewastecollectionservice.service.SystemLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.greensphere.greenspherewastecollectionservice.dto.AppUser;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

@RestController
public class ImageUploadController {

    @Value("${flask.server.url}")
    private String flaskServerUrl;

    @Autowired
    private WasteDataService wasteDataService;

    @Autowired
    private SystemLogsService systemLogsService;

    @PostMapping("/api/v1/upload")
    public ResponseEntity<String> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("weight") BigDecimal weight,
            @RequestParam("location") String location,
            @RequestAttribute("appUser") AppUser appUser) throws IOException {
        String userId=appUser.getUsername();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> response = restTemplate.postForEntity(flaskServerUrl, requestEntity, String.class);
        String result = null;

        try {
            // Parse the JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            int prediction = root.path("prediction").asInt();

            // Map prediction to waste category
            result = switch (prediction) {
                case 0 -> "Cardboard";
                case 1 -> "Glass";
                case 2 -> "Metal";
                case 3 -> "Paper";
                case 4 -> "Plastic";
                case 5 -> "Trash";
                default -> "Unknown";
            };

            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

// Get the current date and format it as a string
        String formattedDate = dateFormat.format(new Date());
//
//        String url = "http://localhost:8080/api/v1/waste/save"; // Adjust the URL to match your endpoint
//
//        // Set headers
//        headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        // Set the request parameters
//        Map<String, String> params = new HashMap<>();
//        params.put("category",result);
//        params.put("collectionDate", formattedDate);
//        params.put("weight",  (weight).toString());
//        params.put("location", location);
//
//        // Create a new HttpEntity
//        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
//
//        // Make the POST request
//        response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//
//        System.out.println("response: " + response.getBody());

//
//
        wasteDataService.saveWasteData(userId, result, formattedDate, weight, location);


        // Update system logs
        systemLogsService.saveSystemLog(userId, "Uploaded file classified as " + result,"Classified"); // Adjust userId

        return ResponseEntity.ok(result);
    }
}
