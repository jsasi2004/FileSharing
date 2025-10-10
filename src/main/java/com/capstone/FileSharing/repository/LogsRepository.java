package com.capstone.FileSharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.FileSharing.model.Logs;

@Repository
public interface LogsRepository extends JpaRepository<Logs,Long> {
    
}