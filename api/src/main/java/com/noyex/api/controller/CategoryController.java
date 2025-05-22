package com.noyex.api.controller;

import com.noyex.data.dtos.CategoryDto;
import com.noyex.data.model.Category;
import com.noyex.service.service.ICategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<Category>> getAllCategories() {
//        List<Category> categories = categoryService.getAllCategories();
//        return ResponseEntity.ok(categories);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
//        Category category = categoryService.getCategoryById(id);
//        return ResponseEntity.ok(category);
//    }

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto) {
        Category createdCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
