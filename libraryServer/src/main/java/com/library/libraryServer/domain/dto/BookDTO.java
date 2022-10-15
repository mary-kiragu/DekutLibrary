package com.library.libraryServer.domain.dto;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import javax.persistence.*;
import java.time.*;
@Data
public class BookDTO {
    private Long id;

    private Long isbn;

    private String accessionNumber;

    private String title;

    private String author;

    private Status status;

    private String borrowedBy;

    private LocalDateTime borrowedOn;

    private String issuedBy;

    private String returnedOn;

    private String imageUrl;
    private String bookImageUrl;

    private String name;

    private String type;

    private String size;
    @Lob
    private byte[] data;


    private Integer categoryId;

    private Integer fine;

    private  String dueDate;
}
