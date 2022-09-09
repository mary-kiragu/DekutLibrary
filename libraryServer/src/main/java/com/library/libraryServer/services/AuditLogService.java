package com.library.libraryServer.services;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.enums.*;
import com.library.libraryServer.repository.*;
import lombok.extern.log4j.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Log4j2
@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog save(AuditLog auditLog) {

        auditLog.setAuditTime(LocalDateTime.now());

        return auditLogRepository.save(auditLog);
    }

    public AuditLog findOne(Long id) {

        Optional<AuditLog> optionalAuditLog = auditLogRepository.findById(id);

        AuditLog auditLog = optionalAuditLog.orElseThrow();

        return auditLog;
    }




    public AuditLog findLastAuditLogByUser(String email){
        log.info("Request to find latest audit log for user : {}",email);

        Optional<AuditLog> optionalAuditLog = auditLogRepository.findTopByUserEmailOrderByIdDesc(email);

        AuditLog auditLog = null;

        if(optionalAuditLog.isPresent()){
            auditLog = optionalAuditLog.get();
        }

        return auditLog;
    }
}
