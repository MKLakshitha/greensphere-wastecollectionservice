package com.greensphere.greenspherewastecollectionservice.service;

import com.greensphere.greenspherewastecollectionservice.model.WasteData;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Interface for WasteDataService operations.
 */
public interface WasteDataService {

    void saveWasteData(Long userId, String category, Date collectionDate, BigDecimal weight, String location);

    void deleteWasteDataById(Long wasteId);

    long countWasteDataByUserId(Long userId);

    long countWasteProcessingByDate(Date date);

    /**
     * Retrieves all waste data records by user ID.
     *
     * @param userId the ID of the user
     * @return a list of waste data records
     */
    List<WasteData> findAllByUserId(Long userId);
}
