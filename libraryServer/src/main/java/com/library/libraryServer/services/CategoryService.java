package com.library.libraryServer.services;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.DTOs.*;
import com.library.libraryServer.domain.enums.*;
import com.library.libraryServer.mapper.*;
import com.library.libraryServer.repository.*;
import com.library.libraryServer.resource.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.library.libraryServer.domain.enums.CategoryEnum.CATEGORY;

@Service
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookResource bookResource;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, BookResource bookResource) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.bookResource = bookResource;
    }
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("About to save category : {}", categoryDTO);

        // prevent passing null values to mapper
        if (categoryDTO.getLevels() == null) {
            categoryDTO.setLevels(new ArrayList<>());
        }
        categoryDTO.setCategoryType(CATEGORY);
        // map categoryDTO to entity for saving
        Category category = categoryMapper.toEntity(categoryDTO);

        categoryDTO.setCategoryType(CATEGORY);

        // call the repo to save category
        category = categoryRepository.save(category);


        return categoryMapper.toDTO(category);
    }

    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        log.info("About to update category  : {}", categoryDTO);

        // check for possible null value for category levels
        if (categoryDTO.getLevels() == null) {
            categoryDTO.setLevels(new ArrayList<>());
        }

        // map categoryDTO to category in readiness for save
        Category category = categoryMapper.toEntity(categoryDTO);

        category = categoryRepository.save(category);

        return categoryMapper.toDTO(category);
    }

    public List<CategoryDTO> getAll() {
        log.info("About to get all categories");
        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        for (Category category : categoryRepository.findAll()) {
            categoryDTOList.add(categoryMapper.toDTO(category));
        }

        return categoryDTOList;
    }
    public void deleteOne(Integer id) {

        log.info("About to delete category with id : {}", id);

        categoryRepository.deleteById(id);
    }

    public CategoryDTO findById(Integer id) {

        log.info("About to find category with id : {}", id);

        Category category = categoryRepository.findById(id).orElseThrow();

        CategoryDTO categoryDTO = categoryMapper.toDTO(category);


        return categoryDTO;
    }

    public List<CategoryDTO> filterByParent(Integer categoryId) {
        log.info("About to get all categories in category : {}", categoryId);
        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        for (Category category : categoryRepository.findByParentCategoryId(categoryId)) {
            categoryDTOList.add(categoryMapper.toDTO(category));
        }

        return categoryDTOList;
    }

}
