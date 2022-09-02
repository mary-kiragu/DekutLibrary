package com.library.libraryServer.mapper;

import com.fasterxml.jackson.databind.*;
import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.DTOs.*;
import org.springframework.stereotype.*;

@Service

public class CategoryMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();


    public CategoryDTO toDTO(Category category){
        if(category==null){
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());

        return categoryDTO;
    }

    public Category toEntity(CategoryDTO categoryDTO){
        if(categoryDTO==null){
            return null;
        }

        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());


        return category;
    }
}
