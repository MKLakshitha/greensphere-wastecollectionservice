package com.greensphere.greenspherewastecollectionservice.controller;

import com.greensphere.greenspherewastecollectionservice.model.WasteData;
import com.greensphere.greenspherewastecollectionservice.service.WasteDataService;
import com.greensphere.greenspherewastecollectionservice.exception.WasteDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Controller for handling waste management operations.
 */
@RestController
@RequestMapping("/api/v1/waste")
public class WasteManagementController {

    private static final Logger logger = LoggerFactory.getLogger(WasteManagementController.class);

    private final WasteDataService wasteDataService;

    @Autowired
    public WasteManagementController(WasteDataService wasteDataService) {
        this.wasteDataService = wasteDataService;
    }

    /**
     * Endpoint to save waste data.
     *
     * @param userId         ID of the user
     * @param category       Category of the waste
     * @param collectionDate Date of waste collection
     * @param weight         Weight of the waste
     * @param location       Location of the waste collection
     * @return ResponseEntity indicating success or failure of the operation
     */
    @PostMapping("/save")
    public ResponseEntity<String> saveWasteData(
            @RequestParam Long userId,
            @RequestParam String category,
            @RequestParam Date collectionDate,
            @RequestParam BigDecimal weight,
            @RequestParam String location) {
        try {
            logger.info("Request received to save waste data for userId: {}", userId);
            wasteDataService.saveWasteData(userId, category, collectionDate, weight, location);
            return ResponseEntity.status(HttpStatus.CREATED).body("Waste data saved successfully.");
        } catch (WasteDataException e) {
            logger.error("Error saving waste data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error saving waste data for userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    /**
     * Endpoint to delete waste data by waste ID.
     *
     * @param wasteId ID of the waste data to delete
     * @return ResponseEntity indicating success or failure of the operation
     */
    @DeleteMapping("/delete/{wasteId}")
    public ResponseEntity<String> deleteWasteDataById(@PathVariable Long wasteId) {
        try {
            logger.info("Request received to delete waste data for wasteId: {}", wasteId);
            wasteDataService.deleteWasteDataById(wasteId);
            return ResponseEntity.status(HttpStatus.OK).body("Waste data deleted successfully.");
        } catch (WasteDataException e) {
            logger.error("Error deleting waste data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error deleting waste data for wasteId: {}", wasteId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    /**
     * Endpoint to get the count of waste data records by user ID.
     *
     * @param userId ID of the user
     * @return ResponseEntity containing the count of waste data records
     */
    @GetMapping("/count/by-user/{userId}")
    public ResponseEntity<String> countWasteDataByUserId(@PathVariable Long userId) {
        try {
            logger.info("Request received to count waste data for userId: {}", userId);
            long count = wasteDataService.countWasteDataByUserId(userId);
            return ResponseEntity.ok("Count of waste data for userId " + userId + ": " + count);
        } catch (Exception e) {
            logger.error("Error counting waste data records for userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    /**
     * Endpoint to get the count of waste processing records by collection date.
     *
     * @param date Date of waste processing
     * @return ResponseEntity containing the count of waste processing records
     */
    @GetMapping("/count/by-date")
    public ResponseEntity<String> countWasteProcessingByDate(@RequestParam Date date) {
        try {
            logger.info("Request received to count waste processing for date: {}", date);
            long count = wasteDataService.countWasteProcessingByDate(date);
            return ResponseEntity.ok("Count of waste processing for date " + date + ": " + count);
        } catch (Exception e) {
            logger.error("Error counting waste processing records for date: {}", date, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
    /**
     * Retrieves all waste data records by user ID.
     *
     * @param userId the ID of the user
     * @return a ResponseEntity containing the list of waste data records and HTTP status
     */
    @GetMapping("/user")
    public List<WasteData> getWasteDataByUserId(@RequestParam Long userId) {
        try {
            List<WasteData> wasteDataList = wasteDataService.findAllByUserId(userId);
            logger.info("Retrieved {} waste data records for userId: {}", wasteDataList.size(), userId);
            return wasteDataList;
        } catch (Exception e) {
            logger.error("Error retrieving waste data for userId: {}", userId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve waste data", e);
        }

    }

}
