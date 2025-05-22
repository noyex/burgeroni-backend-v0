package com.noyex.data.model.cart;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CartSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cookie_id", unique = true, nullable = false)
    private String cookieId;

    @OneToMany(mappedBy = "cartSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItems> items = new ArrayList<>();

    public CartSession() {
    }

    public CartSession(String cookieId) {
        this.cookieId = cookieId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public List<CartItems> getItems() {
        return items;
    }

    public void setItems(List<CartItems> items) {
        this.items = items;
    }

    public void addItem(CartItems item) {
        items.add(item);
        item.setCartSession(this);
    }
}
