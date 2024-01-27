package com.springboot.blog.controller;

import com.springboot.blog.entity.Category;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
     //Build add category  api

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name="Bearer Authentication")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody  CategoryDto categoryDto){
        CategoryDto savedCategory=categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }


    //build get category api

    @GetMapping("{id}")
    public  ResponseEntity<CategoryDto> getCategory(@PathVariable("id") long categoryId){
        CategoryDto categoryDto=categoryService.getCategory(categoryId);
        return  ResponseEntity.ok(categoryDto);
    }

    //build get all category api

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        List<CategoryDto> a=categoryService.getAllCategories();
        return ResponseEntity.ok(a);
    }

    //BUILD UPDATE CATEGORY API
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name="Bearer Authentication")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable("id") long categoryId){
        return ResponseEntity.ok(categoryService.updateCategory(categoryDto,categoryId));
    }

//build delete category api

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name="Bearer Authentication")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") long categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
