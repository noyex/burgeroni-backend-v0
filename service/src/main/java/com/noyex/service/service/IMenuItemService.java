package com.noyex.service.service;

import com.noyex.data.dtos.MenuItemDto;
import com.noyex.data.model.MenuItem;

import java.util.List;

public interface IMenuItemService {
    List<MenuItem> getAllMenuItems();
    MenuItem getMenuItemById(Long id);
    MenuItem createMenuItem(MenuItemDto menuItemDto);
    MenuItem updateMenuItem(Long id, MenuItemDto menuItemDto);
    void deleteMenuItem(Long id);
}
