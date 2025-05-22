package com.noyex.service.service;

import com.noyex.data.dtos.CategoryDto;
import com.noyex.data.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Category createCategory(CategoryDto categoryDto);
    Category updateCategory(Long categoryId, CategoryDto categoryDto);
    void deleteCategory(Long categoryId);
}
