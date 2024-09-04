package com.greensphere.greenspherewastecollectionservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greensphere.greenspherewastecollectionservice.dto.TokenValidationRequest;
import com.greensphere.greenspherewastecollectionservice.dto.ValidateCoreResponse;
import com.greensphere.greenspherewastecollectionservice.exceptions.ApiUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiConnector {

    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    @Value("${is.bypassed}")
    boolean isBypassed;

    public HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    public String sendPostRequest(String url, Object request, HttpHeaders httpHeaders) {
        try {
            log.info("sendPostRequest-> url: " + url);
            log.info("sendPostRequest-> request: " + objectMapper.writeValueAsString(request));

            ResponseEntity<String> response = webClient.post()
                    .uri(url)
                    .headers(header -> header.addAll(httpHeaders))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(request))
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            if (response == null) {
                log.error("sendPostRequest-> response is null");
                return null;
            }
            log.info("sendPostRequest-> response: " + response);
            return response.getBody() != null ? response.getBody() : null;
        } catch (HttpClientErrorException e) {
            log.info("sendPostRequest-> Response Code: " + e.getStatusCode().value());
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                log.warn("sendPostRequest-> Exception: Unauthorized");
            } else if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST) || e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return null;
            } else {
                //Any other HTTP responses
                log.error("sendPostRequest-> Exception: Invalid response received");
            }
            return null;
        } catch (WebClientResponseException e) {
            log.warn("sendPostRequest-> Exception: WebClientResponseException, response: " + e.getResponseBodyAsString());
            if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST) || e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                return e.getResponseBodyAsString();
            } else {
                log.warn("sendPostRequest-> Exception: WebClientResponseException, response_code: " + e.getStatusCode());
                return null;
            }
        } catch (HttpServerErrorException e) {
            log.error("sendPostRequest-> HttpServerErrorException: response Code: " + e.getStatusCode().value());
            log.error("sendPostRequest-> Exception: Service not available");
            throw new ApiUnavailableException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("sendPostRequest-> Exception: " + e.getMessage(), e);
            return null;
        }
    }
    public ValidateCoreResponse tokenValidate(TokenValidationRequest request) throws JsonProcessingException {
        String url = "http://localhost:8082/user/token-validation";
        HttpHeaders httpHeaders = getHttpHeaders();
        String response;

            response = sendPostRequest(url, request, httpHeaders);

        if (response != null) {
            ValidateCoreResponse validateCoreResponse = objectMapper.readValue(response, ValidateCoreResponse.class);
            log.info("validateCoreResponse-> response: " + objectMapper.writeValueAsString(validateCoreResponse));
            return validateCoreResponse;
        }else {
            return null;
        }

    }
}
