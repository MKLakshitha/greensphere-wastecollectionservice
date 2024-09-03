package com.greensphere.greenspherewastecollectionservice.repository;

import com.greensphere.greenspherewastecollectionservice.model.SystemLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemLogsRepository extends JpaRepository<SystemLogs, Long> {
}
