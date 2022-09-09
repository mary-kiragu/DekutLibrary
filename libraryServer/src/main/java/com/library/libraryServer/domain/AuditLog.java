package com.library.libraryServer.domain;


import com.library.libraryServer.domain.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AuditAction auditAction;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String statusReason;

    private String userEmail;

    private String phoneNumber;

    private LocalDateTime auditTime;

    private Long profileId;

    public AuditLog(AuditAction auditAction, TransactionStatus status, String statusReason, String userEmail, String phoneNumber, Long profileId) {
        this.auditAction = auditAction;
        this.status = status;
        this.statusReason = statusReason;
        this.userEmail = userEmail;
        this.profileId = profileId;
        this.auditTime = LocalDateTime.now();
    }


}
