package com.greensphere.greenspherewastecollectionservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateCoreResponse {

    private String code;
    private String title;
    private String message;
    private Data data;
}
