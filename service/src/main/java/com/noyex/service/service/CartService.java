package com.noyex.service.service;

import com.noyex.data.model.MenuItem;
import com.noyex.data.model.cart.CartItems;
import com.noyex.data.model.cart.CartSession;
import com.noyex.data.repository.MenuItemRepository;
import com.noyex.data.repository.cart.CartItemsRepository;
import com.noyex.data.repository.cart.CartSessionRepository;
import com.noyex.service.exceptions.MenuItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class CartService implements ICartService {

    private final CartItemsRepository cartItemsRepository;

    private final CartSessionRepository cartSessionRepository;
    private final MenuItemRepository menuItemRepository;

    public CartService(CartItemsRepository cartItemsRepository, CartSessionRepository cartSessionRepository, MenuItemRepository menuItemRepository) {
        this.cartItemsRepository = cartItemsRepository;
        this.cartSessionRepository = cartSessionRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    @Transactional
    public CartSession getOrCreateCart(String cookieId) {
        Optional<CartSession> cartSession = cartSessionRepository.findByCookieId(cookieId);
        if (cartSession.isPresent()) {
            return cartSession.get();
        } else {
            CartSession newCart = new CartSession(cookieId);
            return cartSessionRepository.save(newCart);
        }
    }

    @Override
    @Transactional
    public void addMenuItemToCart(String cookieId, Long menuItemId) {
        CartSession cart = getOrCreateCart(cookieId);
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found"));
        Optional<CartItems> existingItem = cart.getItems().stream()
                .filter(item -> item.getMenuItemId().equals(menuItemId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItems item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
            cartItemsRepository.save(item);
        } else {
            CartItems newItem = new CartItems(menuItemId, 1);

            cart.addItem(newItem);
            cartSessionRepository.save(cart);
        }
    }

    @Override
    @Transactional
    public void updateCartMenuItemQuantity(String cookieId, Long menuItemId, int quantity) {
        CartSession cart = getOrCreateCart(cookieId);
        Optional<CartItems> existingItem = cart.getItems().stream()
                .filter(item -> item.getMenuItemId().equals(menuItemId))
                .findFirst();
        if (existingItem.isPresent()) {
            CartItems item = existingItem.get();
            if (quantity <= 0) {
                cart.getItems().remove(item);
            } else {
                item.setQuantity(quantity);
            }
            cartItemsRepository.save(item);
        }
    }

    @Override
    @Transactional
    public void removeMenuItemFromCart(String cookieId, Long menuItemId) {
        CartSession cart = getOrCreateCart(cookieId);
        Optional<CartItems> existingItem = cart.getItems().stream()
                .filter(item -> item.getMenuItemId().equals(menuItemId))
                .findFirst();
        if (existingItem.isPresent()) {
            CartItems item = existingItem.get();
            cart.getItems().remove(item);
            cartItemsRepository.delete(item);
        }

    }

    @Override
    @Transactional
    public void clearCart(String cookieId) {
        Optional<CartSession> cartSession = cartSessionRepository.findByCookieId(cookieId);
        cartSession.ifPresent(cart -> {
            cart.getItems().clear();
            cartSessionRepository.save(cart);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPrice(String cookieId) {
        CartSession cart = getOrCreateCart(cookieId);

        BigDecimal totalSum = cart.getItems().stream()
                .map(item -> {
                    MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId())
                            .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found with ID: " + item.getMenuItemId()));

                    BigDecimal price = menuItem.getPrice();

                    BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

                    return price.multiply(quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal roundedSum = totalSum.setScale(2, RoundingMode.HALF_UP);

        return roundedSum;
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalItems(String cookieId) {
        CartSession cart = getOrCreateCart(cookieId);
        return cart.getItems().stream()
                .mapToInt(CartItems::getQuantity)
                .sum();
    }


}
