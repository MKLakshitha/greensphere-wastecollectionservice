package com.greensphere.greenspherewastecollectionservice.repository;

import com.greensphere.greenspherewastecollectionservice.model.WasteData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Repository interface for WasteData entities.
 */
@Repository
public interface WasteDataRepository extends JpaRepository<WasteData, Long> {

    /**
     * Deletes waste data by waste ID.
     *
     * @param wasteId the ID of the waste data to be deleted
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM WasteData wd WHERE wd.id = :wasteId")
    void deleteByWasteId(@Param("wasteId") Long wasteId);

    /**
     * Counts the number of waste data records by user ID.
     *
     * @param userId the ID of the user
     * @return the count of waste data records
     */
    @Query("SELECT COUNT(wd) FROM WasteData wd WHERE wd.userId = :userId")
    long countByUserId(@Param("userId") Long userId);

    /**
     * Counts the number of waste processing records by date.
     *
     * @param date the date of waste processing
     * @return the count of waste processing records
     */
    @Query("SELECT COUNT(wd) FROM WasteData wd WHERE wd.collectionDate = :date")
    long countByCollectionDate(@Param("date") Date date);
}
