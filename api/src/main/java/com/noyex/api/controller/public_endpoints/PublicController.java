package com.noyex.api.controller.public_endpoints;

import com.noyex.api.responses.LoginResponse;
import com.noyex.auth.service.IAuthenticationService;
import com.noyex.auth.service.JwtService;
import com.noyex.data.dtos.LoginUserDto;
import com.noyex.data.dtos.OrderDto;
import com.noyex.data.model.Category;
import com.noyex.data.model.MenuItem;
import com.noyex.data.model.User;
import com.noyex.service.service.ICategoryService;
import com.noyex.service.service.IDeliveryZoneService;
import com.noyex.service.service.IMenuItemService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final IDeliveryZoneService deliveryZoneService;
    private final ICategoryService categoryService;
    private final IMenuItemService menuItemService;
    private final JwtService jwtService;
    private final IAuthenticationService authenticationService;

    public PublicController(IDeliveryZoneService deliveryZoneService, ICategoryService categoryService, IMenuItemService menuItemService, JwtService jwtService, IAuthenticationService authenticationService) {
        this.deliveryZoneService = deliveryZoneService;
        this.categoryService = categoryService;
        this.menuItemService = menuItemService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }


    //delivery zone public endpoint
    @PostMapping("/delivery-zone/is-valid")
    @Cacheable("deliveryZone")
    public ResponseEntity<Boolean> isValid(@RequestBody OrderDto orderDto) {
        boolean isValid = deliveryZoneService.isInDeliveryZone(orderDto);
        return ResponseEntity.ok(isValid);
    }

    //category public endpoints
    @GetMapping("/category/all")
    @Cacheable("category")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @GetMapping("/category/{id}")
    @Cacheable("category")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    //menu item public endpoints
    @GetMapping("/menu-item/all")
    @Cacheable("menuItem")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/menu-item/{id}")
    @Cacheable("menuItem")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        MenuItem menuItem = menuItemService.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }

    //login public endpoint for workers
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDTO){
        User authenticatedUser = authenticationService.authenticate(loginUserDTO);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }
}
