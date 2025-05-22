package com.noyex.api.controller;

import com.noyex.data.dtos.MenuItemDto;
import com.noyex.data.model.MenuItem;
import com.noyex.service.service.IMenuItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/menu-item")
public class MenuItemController {

    private final IMenuItemService menuItemService;

    public MenuItemController(IMenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
//        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
//        return ResponseEntity.ok(menuItems);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
//        MenuItem menuItem = menuItemService.getMenuItemById(id);
//        return ResponseEntity.ok(menuItem);
//    }

    @PostMapping("/create")
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItemDto menuItemDto) {
        MenuItem createdMenuItem = menuItemService.createMenuItem(menuItemDto);
        return ResponseEntity.ok(createdMenuItem);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDto menuItemDto) {
        MenuItem updatedMenuItem = menuItemService.updateMenuItem(id, menuItemDto);
        return ResponseEntity.ok(updatedMenuItem);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok("Menu item deleted successfully");
    }

}
