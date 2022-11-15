package com.library.libraryServer.mapper;

import com.fasterxml.jackson.databind.*;
import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
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
        categoryDTO.setCategoryType(category.getCategoryType());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setParentCategoryId(category.getParentCategoryId());
        categoryDTO.setCategoryImageUrl(category.getCategoryImageUrl());
        categoryDTO.setImageUrl(category.getImageUrl());
        categoryDTO.setCategoryName(category.getCategoryName());
        categoryDTO.setType(category.getType());
        categoryDTO.setData(category.getData());
        categoryDTO.setSize(category.getSize());





        return categoryDTO;
    }

    public Category toEntity(CategoryDTO categoryDTO){
        if(categoryDTO==null){
            return null;
        }

        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setCategoryType(categoryDTO.getCategoryType());
        category.setDescription(categoryDTO.getDescription());
        category.setParentCategoryId(categoryDTO.getParentCategoryId());
        category.setCategoryImageUrl(categoryDTO.getCategoryImageUrl());
        category.setImageUrl(categoryDTO.getImageUrl());
        category.setCategoryName(category.getCategoryName());
        category.setType(categoryDTO.getType());
        category.setData(categoryDTO.getData());
        category.setSize(categoryDTO.getSize());


        return category;
    }
}
