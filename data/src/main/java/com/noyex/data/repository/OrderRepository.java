package com.noyex.data.repository;

import com.noyex.data.model.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
