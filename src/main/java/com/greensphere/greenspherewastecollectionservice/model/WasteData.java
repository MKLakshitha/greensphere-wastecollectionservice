package com.greensphere.greenspherewastecollectionservice.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Entity
@Table(name = "wastedata")
public class WasteData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "category",  length = 50)
    private String category;

    @Column(name = "collection_date")
    @Temporal(TemporalType.DATE)
    private String collectionDate;

    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "location", length = 255)
    private String location;

    // Getters and Setters
    // (Add getter and setter for the location field)

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
