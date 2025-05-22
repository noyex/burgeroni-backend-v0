package com.noyex.service.service;

import com.noyex.data.dtos.MenuItemDto;
import com.noyex.data.model.Category;
import com.noyex.data.model.MenuItem;
import com.noyex.data.repository.CategoryRepository;
import com.noyex.data.repository.MenuItemRepository;
import com.noyex.service.exceptions.CategoryNotFoundException;
import com.noyex.service.exceptions.MenuItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MenuItemService implements IMenuItemService{

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Override
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
    }

    @Override
    @Transactional
    public MenuItem createMenuItem(MenuItemDto menuItemDto) {
        MenuItem menuItem = new MenuItem();
        return menuItemRepository.save(entityToDtoMapper(menuItemDto, menuItem));
    }

    @Override
    @Transactional
    public MenuItem updateMenuItem(Long id, MenuItemDto menuItemDto) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found with id: " + id));
        return menuItemRepository.save(entityToDtoMapper(menuItemDto, menuItem));
    }

    @Override
    @Transactional
    public void deleteMenuItem(Long id) {
        MenuItem menuItemToDelete = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found with id: " + id));
        menuItemRepository.delete(menuItemToDelete);
    }

// helpful methods
    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
    }

    private MenuItem entityToDtoMapper(MenuItemDto menuItemDto, MenuItem menuItem) {
        menuItem.setName(menuItemDto.getName());
        menuItem.setDescription(menuItemDto.getDescription());
        String price = menuItemDto.getPrice().toString();
        BigDecimal decimalPrice = new BigDecimal(price);
        menuItem.setPrice(decimalPrice);
        menuItem.setImageUrl(menuItemDto.getImageUrl());

        Category category = getCategoryById(menuItemDto.getCategoryId());
        menuItem.setCategory(category);
        return menuItem;
    }
}
