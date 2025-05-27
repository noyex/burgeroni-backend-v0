package com.noyex.data.repository;

import com.noyex.data.model.MenuItem;
import com.noyex.data.model.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
