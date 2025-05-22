package com.noyex.service.service;

import com.noyex.data.dtos.OrderDto;
import com.noyex.data.dtos.OrderOnSiteDto;
import com.noyex.data.model.order.Orders;

import java.util.List;

public interface IOrderService {
    List<Orders> getAllOrders();
    Orders createOrder(OrderDto orderDto, String cookieId, boolean isDelivery, boolean isCardPayment);
    Orders createOrderOnSite(OrderOnSiteDto orderDto, boolean isDelivery, boolean isCardPayment);
    Orders getOrderById(Long orderId);
    void updateTotalPriceOrderOnSite(Orders order);
    void addMenuItemToOrder(Orders order, Long menuItemId);
    void deleteMenuItemFromOrder(Orders order, Long menuItemId);
    void deleteOrderOnSite(Long orderId);
    void acceptOrder(Long orderId);
    void cancelOrder(Long orderId);
    void changeOrderStatusToInProgress(Long orderId);
    void changeOrderStatusToDone(Long orderId);
    void changeOrderStatusToInDelivery(Long orderId);
}
