package com.noyex.data.repository.cart;

import com.noyex.data.model.cart.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
}
