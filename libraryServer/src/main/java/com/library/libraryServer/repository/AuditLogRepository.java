package com.library.libraryServer.repository;

import com.library.libraryServer.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Optional<AuditLog> findTopByUserEmailOrderByIdDesc(String email);
}
