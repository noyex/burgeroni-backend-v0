package com.noyex.data.repository;

import com.noyex.data.model.order.OrderItems;
import com.noyex.data.model.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {
    List<OrderItems> findAllByOrders(Orders orders);
}
