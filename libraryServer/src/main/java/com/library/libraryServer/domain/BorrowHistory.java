package com.library.libraryServer.domain;

import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.library.libraryServer.domain.enums.*;
import lombok.*;

import javax.persistence.*;
import java.time.*;

@Data
@Entity
@Table(name = "tbl_borrow_history")
@AllArgsConstructor
@NoArgsConstructor
public class BorrowHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book  book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User  user;

    @Enumerated(EnumType.STRING)
    private Actions action;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdOn;

    private LocalDateTime dueDate;



}
