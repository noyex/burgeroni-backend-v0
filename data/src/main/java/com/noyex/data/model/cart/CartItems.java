package com.noyex.data.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.noyex.data.model.MenuItem;
import jakarta.persistence.*;

@Entity
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_session_id", nullable = false)
    @JsonIgnoreProperties({"items"})
    private CartSession cartSession;

    private Long menuItemId;

    private int quantity;

    public CartItems() {
    }

    public CartItems(Long menuItemId, int quantity) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartSession getCartSession() {
        return cartSession;
    }

    public void setCartSession(CartSession cartSession) {
        this.cartSession = cartSession;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
