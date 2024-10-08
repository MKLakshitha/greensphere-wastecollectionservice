package com.greensphere.greenspherewastecollectionservice.service;

import com.greensphere.greenspherewastecollectionservice.model.SystemLogs;
import com.greensphere.greenspherewastecollectionservice.repository.SystemLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SystemLogsService {

    @Autowired
    private SystemLogsRepository systemLogsRepository;

    public SystemLogs saveSystemLog(String userId, String action,String Details) {
        SystemLogs systemLogs = new SystemLogs();
        systemLogs.setUserId(userId);
        systemLogs.setAction(action);
        systemLogs.setDetails(Details);
        systemLogs.setTimestamp(new Date());

        return systemLogsRepository.save(systemLogs);
    }
}
