package com.capstone.FileSharing.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.capstone.FileSharing.model.Logs;
import com.capstone.FileSharing.repository.LogsRepository;

// import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogsService {
    private final LogsRepository logsRepository;
    public void saveLog(Logs logs){
        logsRepository.save(logs);
    }
    // @PostConstruct
    void checking(){
        Logs logs = new Logs();
        logs.setAction("action");
        logs.setCode("faj;");
        logs.setFilename("fajl");
        logs.setTime(new Date());
        logs.setWho("fkah");
        logsRepository.save(logs);
    }
}
