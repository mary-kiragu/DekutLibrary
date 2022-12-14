package com.library.libraryServer.domain;

import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.library.libraryServer.domain.enums.*;
import lombok.*;

import javax.persistence.*;
import java.io.*;
import java.time.*;

@Data
@Entity
@Table(name = "tbl_book")
public class Book implements Serializable {

    public Book() {

    }

    public Book(Long id, String title, String author, Status status, String borrowedBy, LocalDateTime borrowedOn, String issuedBy, LocalDateTime returnedOn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;
        this.borrowedBy = borrowedBy;
        this.borrowedOn =borrowedOn;
        this.issuedBy = issuedBy;
        this.returnedOn = returnedOn;
    }

    public Book(String title, String author, Status status, String borrowedBy, LocalDateTime borrowedOn, String issuedBy, LocalDateTime returnedOn) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.borrowedBy = borrowedBy;
        // this.borrowedOn = borrowedOn;
        this.issuedBy = issuedBy;
        //this.returnedOn = returnedOn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long isbn;
    private String title;
    private String author;

    private String description;

    private String accessionNumber;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String borrowedBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime borrowedOn;
    private String issuedBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime issuedOn;
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

    private Long Copies;

    private Integer categoryId;

    private Integer fine=0;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private  LocalDateTime dueDate;

    private String bookUrl;

    private String bookName;

    private String bookType;

    private String bookSize;
    @Lob
    private byte[] bookData;


}
