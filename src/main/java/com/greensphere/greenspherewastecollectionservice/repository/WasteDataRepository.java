package com.greensphere.greenspherewastecollectionservice.repository;

import com.greensphere.greenspherewastecollectionservice.model.WasteData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    long countByUserId(@Param("userId") String userId);

    /**
     * Counts the number of waste processing records by date.
     *
     * @param date the date of waste processing
     * @return the count of waste processing records
     */
    @Query("SELECT COUNT(wd) FROM WasteData wd WHERE wd.collectionDate = :date")
    long countByCollectionDate(@Param("date") String date);


    /**
     * Counts the number of waste processing records by date.
     *
     * @param category the date of waste processing
     * @return the count of waste processing records
     */
    @Query("SELECT COUNT(wd) FROM WasteData wd WHERE wd.category = :category")
    long countByCategory(@Param("category") String category);


    /**
     * Retrieves all waste data records by user ID.
     *
     * @param userId the ID of the user
     * @return a list of waste data records
     */
    @Query("SELECT wd FROM WasteData wd WHERE wd.userId = :userId")
    List<WasteData> findAllByUserId(@Param("userId") String userId);
}
