package com.noyex.api.controller.public_endpoints;

import com.noyex.data.dtos.OrderDto;
import com.noyex.data.model.order.Orders;
import com.noyex.data.model.cart.CartSession;
import com.noyex.service.service.ICartService;
import com.noyex.service.service.IOrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final ICartService cartService;
    private final IOrderService orderService;


    public CartController(ICartService cartService, IOrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @Value("${COOKIE_NAME}")
    private String COOKIE_NAME;

    @PostMapping("/add-to-cart/{menuItemId}")
    public ResponseEntity<String> addToCart(HttpServletRequest request, HttpServletResponse response, @PathVariable Long menuItemId) {
        String cookieId = getOrCreateCookieId(request, response);
        cartService.addMenuItemToCart(cookieId, menuItemId);
        return ResponseEntity.ok("Dodano do koszyka");
    }

    @GetMapping("/view-cart")
    public ResponseEntity<CartSession> viewCart(HttpServletRequest request, HttpServletResponse response, Model model) {
        String cookieId = getOrCreateCookieId(request, response);
        CartSession cart = cartService.getOrCreateCart(cookieId);
        model.addAttribute("cart", cart.getItems());
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/total-price")
    public ResponseEntity<BigDecimal> getTotalPrice(HttpServletRequest request, HttpServletResponse response, Model model) {
        String cookieId = getOrCreateCookieId(request, response);
        CartSession cart = cartService.getOrCreateCart(cookieId);
        BigDecimal totalPrice = cartService.getTotalPrice(cookieId);
        return ResponseEntity.ok(totalPrice);
    }

    @PostMapping("/clear-cart")
    public ResponseEntity<String> clearCart(HttpServletRequest request, HttpServletResponse response) {
        String cookieId = getOrCreateCookieId(request, response);
        cartService.clearCart(cookieId);
        return ResponseEntity.ok("Koszyk został opróżniony");
    }

    @GetMapping("/total-items")
    public ResponseEntity<Integer> getTotalItems(HttpServletRequest request, HttpServletResponse response) {
        String cookieId = getOrCreateCookieId(request, response);
        CartSession cart = cartService.getOrCreateCart(cookieId);
        Integer totalItems = cartService.getTotalItems(cookieId);
        return ResponseEntity.ok(totalItems);
    }

    @PutMapping("/update-cart/{menuItemId}/{quantity}")
    public ResponseEntity<String> updateCartItemQuantity(HttpServletRequest request, HttpServletResponse response, @PathVariable Long menuItemId, @PathVariable int quantity) {
        String cookieId = getOrCreateCookieId(request, response);
        cartService.updateCartMenuItemQuantity(cookieId, menuItemId, quantity);
        return ResponseEntity.ok("Ilość przedmiotu w koszyku została zaktualizowana");
    }

    @DeleteMapping("/remove-item/{menuItemId}")
    public ResponseEntity<String> removeItemFromCart(HttpServletRequest request, HttpServletResponse response, @PathVariable Long menuItemId) {
        String cookieId = getOrCreateCookieId(request, response);
        cartService.removeMenuItemFromCart(cookieId, menuItemId);
        return ResponseEntity.ok("Przedmiot został usunięty z koszyka");
    }

    @PostMapping("/checkout")
    public ResponseEntity<Orders> checkout(@RequestBody OrderDto orderDto, HttpServletRequest request, HttpServletResponse response, @RequestParam Boolean isDelivery, @RequestParam Boolean isCardPayment) {
        String cookieId = getOrCreateCookieId(request, response);
        Orders order = orderService.createOrder(orderDto, cookieId, isDelivery, isCardPayment);
        cartService.clearCart(cookieId);
        return ResponseEntity.ok(order);
    }

    private String getOrCreateCookieId(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        String cookieId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(COOKIE_NAME, cookieId);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
        return cookieId;
    }
}
