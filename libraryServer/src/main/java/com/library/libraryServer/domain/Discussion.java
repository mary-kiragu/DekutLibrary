package com.library.libraryServer.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_comments")
@AllArgsConstructor
@NoArgsConstructor
public class Discussion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String content;

    private String createdBy;

    private String createdOn;

    private Long book;

    private Long referencedCommentId;

    private String lastUpdatedOn;

    private String lastUpdatedBy;
}
