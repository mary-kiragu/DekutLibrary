package com.library.libraryServer.domain.DTOs;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import javax.persistence.*;
import java.time.*;
@Data
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private Status status;
    private String borrowedBy;
    private LocalDateTime borrowedOn;
    private String issuedBy;
    private String returnedOn;
    private String imageUrl;
}
