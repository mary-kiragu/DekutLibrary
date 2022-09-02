package com.library.libraryServer.domain;

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
        this.borrowedOn = borrowedOn;
        this.issuedBy = issuedBy;
        this.returnedOn = returnedOn;
    }

    public Book(String title, String author, Status status, String borrowedBy, LocalDateTime borrowedOn, String issuedBy, LocalDateTime returnedOn) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.borrowedBy = borrowedBy;
        this.borrowedOn = borrowedOn;
        this.issuedBy = issuedBy;
        this.returnedOn = returnedOn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String borrowedBy;
    private LocalDateTime borrowedOn;
    private String issuedBy;
    private LocalDateTime returnedOn;

    private String imageUrl;

    private Long Copies;

    private Integer categoryId;



}
