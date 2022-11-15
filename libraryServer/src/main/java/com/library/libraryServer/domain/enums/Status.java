package com.library.libraryServer.domain.enums;

import javax.xml.bind.annotation.*;

public enum Status {
    AVAILABLE,
    NEW,
    BORROWED,
    RETURNED,
    ISSUED
}
