package com.library.libraryServer.resource;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.services.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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
        return categoryService.createCategory(categoryDTO);
    }
    @PutMapping("/categories")
    public CategoryDTO updateCategory( @RequestBody CategoryDTO categoryDTO){
        return categoryService.updateCategory(categoryDTO);
    }
    @GetMapping("/categories")
    List<CategoryDTO> getAllCategories(){
        return categoryService.getAll();
    }
    @GetMapping("/categories/{id}")
    public CategoryDTO  getOne(@PathVariable Long id) {

        return categoryService.findById(Math.toIntExact(id));
    }
    @GetMapping("/categories/filter-by-parent/{categoryId}")
    public List<CategoryDTO> filterByParentCategory(@PathVariable Integer categoryId, @RequestParam(required = false) Boolean adminView) {

        List<CategoryDTO>categoryDTOS = categoryService.filterByParent(categoryId);

               return categoryDTOS;
    }

    @DeleteMapping
    ResponseEntity deleteCategory(@PathVariable("id") int id){
        Book deletedBook=categoryService.deleteBook(id);
        return new ResponseEntity(deletedBook,HttpStatus.OK);
    }

}
