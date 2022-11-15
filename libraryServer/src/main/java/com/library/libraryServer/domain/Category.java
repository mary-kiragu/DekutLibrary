package com.library.libraryServer.domain;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import javax.persistence.*;
@Data
@Entity
@Table(name="tbl_categories")
public class Category {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(length = 50, nullable = false, unique = true)
        private String name;

        @Column(name = "parent_category_id")
        private Integer parentCategoryId;

        @Column(length = 2000)
        private String description;
        
        @Column(length = 2000)
        private CategoryEnum categoryType;

        private String imageUrl;

        private String categoryImageUrl;

        private String categoryName;

        private String type;

        private String size;
        @Lob
        private byte[] data;

    }

