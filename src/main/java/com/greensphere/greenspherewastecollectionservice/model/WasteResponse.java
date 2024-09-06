package com.greensphere.greenspherewastecollectionservice.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class WasteResponse {
    // Getters and Setters
    private String category;
    private String collectionDate;
    private BigDecimal weight;
    private double price;
    private int points;

}
