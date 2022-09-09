package com.library.libraryServer.domain.dto;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import java.time.*;
@Data
public class BookDTO {
    private Long id;
    private Long isbn;
    private String title;
    private String author;
    private Status status;
    private String borrowedBy;
    private LocalDateTime borrowedOn;
    private String issuedBy;
    private String returnedOn;
    private String imageUrl;
    private Integer categoryId;
}
