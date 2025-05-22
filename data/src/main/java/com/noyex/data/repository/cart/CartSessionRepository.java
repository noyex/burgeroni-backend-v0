package com.noyex.data.repository.cart;

import com.noyex.data.model.cart.CartSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartSessionRepository extends JpaRepository<CartSession, Long> {
    Optional<CartSession> findByCookieId(String cookieId);
}
