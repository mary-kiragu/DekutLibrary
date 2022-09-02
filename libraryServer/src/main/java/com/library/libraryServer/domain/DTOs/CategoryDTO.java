package com.library.libraryServer.domain.DTOs;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import java.util.*;
@Data
public class CategoryDTO {
    private Integer id;

    private String name;

    private List<String> levels;

    private List<CategoryDTO> parents;

    private CategoryEnum categoryType=CategoryEnum.CATEGORY;

}
