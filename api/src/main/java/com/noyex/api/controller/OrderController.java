package com.noyex.api.controller;

import com.noyex.data.dtos.OrderOnSiteDto;
import com.noyex.data.model.order.Orders;
import com.noyex.service.service.IOrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authorized/orders")
public class OrderController {

    @Value("${COOKIE_NAME}")
    private String COOKIE_NAME;

    private final IOrderService orderService;


    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Orders>> getAllOrders() {
        List<Orders> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long orderId) {
        Orders order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createOrderOnSite(@RequestBody OrderOnSiteDto orderOnSiteDto, @RequestParam Boolean isDelivery, @RequestParam Boolean isCardPayment) {
        orderService.createOrderOnSite(orderOnSiteDto, isDelivery, isCardPayment);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-item-to-order/{orderId}/{menuItemId}")
    public ResponseEntity<Void> addMenuItemToOrder(@PathVariable Long orderId, @PathVariable Long menuItemId) {
        Orders order = orderService.getOrderById(orderId);
        orderService.addMenuItemToOrder(order, menuItemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete-item-from-order/{orderId}/{menuItemId}")
    public ResponseEntity<Void> deleteMenuItemFromOrder(@PathVariable Long orderId, @PathVariable Long menuItemId) {
        Orders order = orderService.getOrderById(orderId);
        orderService.deleteMenuItemFromOrder(order, menuItemId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-order/{orderId}")
    public ResponseEntity<Void> deleteOrderOnSite(@PathVariable Long orderId) {
        orderService.deleteOrderOnSite(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-total-price/{orderId}")
    public ResponseEntity<Void> updateTotalPriceOrderOnSite(@PathVariable Long orderId) {
        Orders order = orderService.getOrderById(orderId);
        orderService.updateTotalPriceOrderOnSite(order);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/accept-order/{orderId}")
    public ResponseEntity<Void> acceptOrder(@PathVariable Long orderId) {
        orderService.acceptOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cancel-order/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/set-order-in-progress/{orderId}")
    public ResponseEntity<Void> setOrderStatusToInProgress(@PathVariable Long orderId) {
        orderService.changeOrderStatusToInProgress(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/set-order-done/{orderId}")
    public ResponseEntity<Void> setOrderStatusToDone(@PathVariable Long orderId) {
        orderService.changeOrderStatusToDone(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/set-order-in-delivery/{orderId}")
    public ResponseEntity<Void> setOrderStatusToInDelivery(@PathVariable Long orderId) {
        orderService.changeOrderStatusToInDelivery(orderId);
        return ResponseEntity.ok().build();
    }
}
