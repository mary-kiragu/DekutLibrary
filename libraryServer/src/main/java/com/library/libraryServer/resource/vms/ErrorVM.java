package com.library.libraryServer.resource.vms;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

@Data
public class ErrorVM {
    private ErrorStatus status;

    private String statusReason;

    public ErrorVM(ErrorStatus status, String statusReason) {
        this.status = status;
        this.statusReason = statusReason;
    }

    public ErrorVM(String statusReason) {
        this.status = ErrorStatus.FAILURE.FAILURE;
        this.statusReason = statusReason;
    }

    public ErrorVM() {
    }
}

