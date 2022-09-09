package com.library.libraryServer.domain.dto;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import java.util.*;
@Data
public class CategoryDTO {
    private Integer id;

    private String name;

    private  Integer parentCategoryId;

    private CategoryEnum categoryType;

    private String description;

    private String imageURL;



}
