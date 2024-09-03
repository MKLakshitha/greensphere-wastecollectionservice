package com.greensphere.greenspherewastecollectionservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greensphere.greenspherewastecollectionservice.service.WasteDataService;
import com.greensphere.greenspherewastecollectionservice.service.SystemLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

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
            @RequestParam("location") String location) throws IOException {

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

        // Save the waste data including weight and location
        wasteDataService.saveWasteData(1L, result, new Date(), weight, location); // Adjust userId

        // Update system logs
        systemLogsService.saveSystemLog(1L, "Uploaded file classified as " + result); // Adjust userId

        return ResponseEntity.ok(result);
    }
}
