package com.library.libraryServer.domain.dto;

import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
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
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime borrowedOn;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime issuedOn;

    private String issuedBy;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime returnedOn;

    private String imageUrl;
    private String bookImageUrl;

    private String name;

    private String type;

    private String size;
    @Lob
    private byte[] data;


    private Integer categoryId;

    private Integer fine;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private  LocalDateTime dueDate;
//book file url
    private String bookUrl;

    private String bookName;

    private String bookType;

    private String bookSize;
    @Lob
    private byte[] bookData;
}
