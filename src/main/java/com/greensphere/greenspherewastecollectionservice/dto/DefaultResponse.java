package com.greensphere.greenspherewastecollectionservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@Builder
public class DefaultResponse {

    private String code;
    private String title;
    private String message;
    private Object data;

}
