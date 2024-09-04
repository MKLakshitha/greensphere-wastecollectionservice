package com.greensphere.greenspherewastecollectionservice.service.impl;

import com.greensphere.greenspherewastecollectionservice.model.WasteData;
import com.greensphere.greenspherewastecollectionservice.repository.WasteDataRepository;
import com.greensphere.greenspherewastecollectionservice.service.WasteDataService;
import com.greensphere.greenspherewastecollectionservice.util.WasteDataConstants;
import com.greensphere.greenspherewastecollectionservice.exception.WasteDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Implementation of WasteDataService interface for managing waste data.
 */
@Service
public class WasteDataServiceImpl implements WasteDataService {

    private static final Logger logger = LoggerFactory.getLogger(WasteDataServiceImpl.class);

    private final WasteDataRepository wasteDataRepository;

    @Autowired
    public WasteDataServiceImpl(WasteDataRepository wasteDataRepository) {
        this.wasteDataRepository = wasteDataRepository;
    }

    /**
     * Saves waste data to the database.
     *
     * @param userId         ID of the user
     * @param category       Category of the waste
     * @param collectionDate Date when waste was collected
     * @param weight         Weight of the waste
     * @param location       Location of waste collection
     */
    @Override
    public void saveWasteData(Long userId, String category, Date collectionDate, BigDecimal weight, String location) {
        try {
            validateWasteData(userId, category, weight, location);

            WasteData wasteData = new WasteData();
            wasteData.setUserId(userId);
            wasteData.setCategory(category);
            wasteData.setCollectionDate(collectionDate);
            wasteData.setWeight(weight);
            wasteData.setLocation(location);

            wasteDataRepository.save(wasteData);
            logger.info("Waste data saved successfully for userId: {}", userId);
        } catch (WasteDataException e) {
            logger.error("Error saving waste data: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error saving waste data for userId: {}", userId, e);
            throw new WasteDataException("An unexpected error occurred while saving waste data", e);
        }
    }

    /**
     * Retrieves all waste data records by user ID.
     *
     * @param userId the ID of the user
     * @return a list of waste data records
     */
    @Override
    public List<WasteData> findAllByUserId(Long userId) {
        try {
            List<WasteData> wasteDataList = wasteDataRepository.findAllByUserId(userId);
            logger.info("Retrieved {} waste data records for userId: {}", wasteDataList.size(), userId);
            return wasteDataList;
        } catch (Exception e) {
            logger.error("Error retrieving waste data records for userId: {}", userId, e);
            throw new WasteDataException("An error occurred while retrieving waste data records", e);
        }
    }

    /**
     * Deletes waste data by its waste ID.
     *
     * @param wasteId the ID of the waste data to delete
     */
    @Override
    public void deleteWasteDataById(Long wasteId) {
        try {
            wasteDataRepository.deleteByWasteId(wasteId);
            logger.info("Waste data deleted successfully for wasteId: {}", wasteId);
        } catch (Exception e) {
            logger.error("Error deleting waste data for wasteId: {}", wasteId, e);
            throw new WasteDataException("An error occurred while deleting waste data", e);
        }
    }

    /**
     * Counts the waste data records by user ID.
     *
     * @param userId the ID of the user
     * @return the count of waste data records
     */
    @Override
    public long countWasteDataByUserId(Long userId) {
        try {
            long count = wasteDataRepository.countByUserId(userId);
            logger.info("Count of waste data for userId {}: {}", userId, count);
            return count;
        } catch (Exception e) {
            logger.error("Error counting waste data records for userId: {}", userId, e);
            throw new WasteDataException("An error occurred while counting waste data records", e);
        }
    }

    /**
     * Counts the waste processing records by collection date.
     *
     * @param date the date of waste processing
     * @return the count of waste processing records
     */
    @Override
    public long countWasteProcessingByDate(Date date) {
        try {
            long count = wasteDataRepository.countByCollectionDate(date);
            logger.info("Count of waste processing for date {}: {}", date, count);
            return count;
        } catch (Exception e) {
            logger.error("Error counting waste processing records for date: {}", date, e);
            throw new WasteDataException("An error occurred while counting waste processing records", e);
        }
    }



    /**
     * Validates waste data fields.
     *
     * @param userId   ID of the user
     * @param category Category of the waste
     * @param weight   Weight of the waste
     * @param location Location of waste collection
     * @throws WasteDataException if any validation fails
     */
    private void validateWasteData(Long userId, String category, BigDecimal weight, String location) {
        if (userId == null || userId <= 0) {
            throw new WasteDataException(WasteDataConstants.INVALID_USER_ID);
        }
        if (category == null || category.trim().isEmpty()) {
            throw new WasteDataException(WasteDataConstants.INVALID_CATEGORY);
        }
        if (weight == null || weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WasteDataException(WasteDataConstants.INVALID_WEIGHT);
        }
        if (location == null || location.trim().isEmpty()) {
            throw new WasteDataException(WasteDataConstants.INVALID_LOCATION);
        }
    }
}
