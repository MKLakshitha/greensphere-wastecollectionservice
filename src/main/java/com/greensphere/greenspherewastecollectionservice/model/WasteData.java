package com.greensphere.greenspherewastecollectionservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wastedata")
@Getter
@Setter
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
    private Date collectionDate;

    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "location", length = 255)
    private String location;

}