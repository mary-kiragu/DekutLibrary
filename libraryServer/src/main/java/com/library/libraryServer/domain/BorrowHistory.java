package com.library.libraryServer.domain;

import lombok.*;

import javax.persistence.*;

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



}
