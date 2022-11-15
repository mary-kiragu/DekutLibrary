package com.library.libraryServer.resource;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.services.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class CategoryResource {
    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO){
        categoryDTO.setCategoryImageUrl(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/downloadCategoryPicture/")
                .path(String.valueOf(categoryDTO.getId()))
                .toUriString());
        return categoryService.createCategory(categoryDTO);
    }
    @PutMapping("/categories")
    public CategoryDTO updateCategory( @RequestBody CategoryDTO categoryDTO){
        log.info("About to update category with id : {}",categoryDTO);
        
        categoryDTO.setCategoryImageUrl(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/downloadCategoryPicture/")
                .path(String.valueOf(categoryDTO.getId()))
                .toUriString());
        return categoryService.updateCategory(categoryDTO);
    }

    @GetMapping("/downloadCategoryPicture/{id}")
    public ResponseEntity<byte[]> downloadCategoryPicture(@PathVariable Integer id) {
        Category category = categoryService.downloadCategoryPicture((id));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + category.getCategoryName() + "\"")
                .body(category.getData());
    }
    @GetMapping("/categories")
    List<CategoryDTO> getAllCategories(){
        List<CategoryDTO> allCategories = categoryService.getAll();
        for (CategoryDTO categoryDTO : allCategories){
            categoryDTO.setCategoryImageUrl(ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/downloadCategoryPicture/")
                    .path(String.valueOf(categoryDTO.getId()))
                    .toUriString());

        }
        return allCategories;
    }
    @GetMapping("/categories/{id}")
    public CategoryDTO  getOne(@PathVariable Integer id) {
        CategoryDTO foundCategory=categoryService.findById(id);
        foundCategory.setCategoryImageUrl(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/downloadCategoryPicture/")
                .path(String.valueOf(foundCategory.getId()))
                .toUriString());


        return foundCategory;
    }
    @GetMapping("/categories/filter-by-parent/{categoryId}")
    public List<CategoryDTO> filterByParentCategory(@PathVariable Integer categoryId) {

        List<CategoryDTO> categoryDTOS = categoryService.filterByParent(categoryId);
        for (CategoryDTO categoryDTO : categoryDTOS){
            categoryDTO.setCategoryImageUrl(ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/downloadCategoryPicture/")
                    .path(String.valueOf(categoryDTO.getId()))
                    .toUriString());

        }

               return categoryDTOS;
    }

    @DeleteMapping(path="/categories/{id}")
    ResponseEntity deleteCategory(@PathVariable("id") int id){
        Book deletedBook=categoryService.deleteBook(id);
        return new ResponseEntity(deletedBook,HttpStatus.OK);
    }

    @GetMapping(path= "/categories/search")
    public List<Category> findAll(@RequestParam(required = false) String text) {
        log.debug("REST request to search all categories with text : {}", text);

        if (text  == null) {
            text = "";
        }

        return categoryService.search(text);
    }

}
