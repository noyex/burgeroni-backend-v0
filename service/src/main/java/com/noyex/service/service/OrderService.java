package com.noyex.service.service;

import com.noyex.data.dtos.OrderDto;
import com.noyex.data.dtos.OrderOnSiteDto;
import com.noyex.data.enums.OrderStatus;
import com.noyex.data.enums.OrderType;
import com.noyex.data.enums.PaymentMethod;
import com.noyex.data.model.MenuItem;
import com.noyex.data.model.order.OrderItems;
import com.noyex.data.model.order.Orders;
import com.noyex.data.model.cart.CartItems;
import com.noyex.data.model.cart.CartSession;
import com.noyex.data.repository.MenuItemRepository;
import com.noyex.data.repository.OrderItemRepository;
import com.noyex.data.repository.OrderRepository;
import com.noyex.data.repository.cart.CartSessionRepository;
import com.noyex.service.exceptions.MenuItemNotFoundException;
import com.noyex.service.exceptions.OrderNotFoundException;
import com.noyex.service.utils.OrderNumberGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final CartSessionRepository cartSessionRepository;
    private final MenuItemRepository menuItemRepository;
    private final CartService cartService;
    private final IEmailService emailService;
    private final OrderItemRepository orderItemRepository;
    private final OrderNumberGenerator orderNumberGenerator;

    BigDecimal deliveryPrice = new BigDecimal("7.00");
    BigDecimal zero = new BigDecimal("0.00");

    public OrderService(OrderRepository orderRepository, CartSessionRepository cartSessionRepository, MenuItemRepository menuItemRepository, CartService cartService, IEmailService emailService, OrderItemRepository orderItemRepository, OrderNumberGenerator orderNumberGenerator) {
        this.orderRepository = orderRepository;
        this.cartSessionRepository = cartSessionRepository;
        this.menuItemRepository = menuItemRepository;
        this.cartService = cartService;
        this.emailService = emailService;
        this.orderItemRepository = orderItemRepository;
        this.orderNumberGenerator = orderNumberGenerator;
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Orders createOrder(OrderDto orderDto, String cookieId, boolean isDelivery, boolean isCardPayment) {
        CartSession cartSession = cartSessionRepository.findByCookieId(cookieId)
                .orElseThrow(() -> new RuntimeException("Cart session not found"));

        BigDecimal cartTotal = cartService.getTotalPrice(cookieId);
        BigDecimal cartTotalWithDelivery = cartTotal.add(deliveryPrice);

        Orders order = new Orders();
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderNumber("#" + orderNumberGenerator.generateOrderNumber());
        order.setFirstName(orderDto.getFirstName());
        order.setLastName(orderDto.getLastName());
        order.setPhone(orderDto.getPhone());
        order.setUserEmail(orderDto.getUserEmail());
        order.setComment(orderDto.getComment());
        order.setOrderStatus(OrderStatus.NIE_ZAAKCEPTOWANE);
        order.setDelivery(isDelivery);
        order.setTotalPrice(cartTotal);
        order.setOrderType(OrderType.ONLINE);
        if(isDelivery) {
            order.setAddress(orderDto.getAddress());
            order.setAddressSecondLine(orderDto.getAddressSecondLine());
            order.setCity(orderDto.getCity());
            order.setZipCode(orderDto.getZipCode());
            order.setTotalPrice(cartTotalWithDelivery);
            if(isCardPayment) {
                order.setPaymentMethod(PaymentMethod.CARD);
            } else {
                order.setPaymentMethod(PaymentMethod.CASH);
            }
        }
        order.setItems(getMenuItemsFromCart(cartSession, order));
        orderRepository.save(order);
        emailService.sendOrderConfirmation(order);
        return order;
        }

    @Override
    @Transactional
    public Orders createOrderOnSite(OrderOnSiteDto orderDto, boolean isDelivery, boolean isCardPayment) {
        Orders order = new Orders();
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderNumber("#" + orderNumberGenerator.generateOrderNumber());
        order.setComment(orderDto.getComment());
        if(isDelivery) {
            order.setFirstName(orderDto.getFirstName());
            order.setLastName(orderDto.getLastName());
            order.setPhone(orderDto.getPhone());
            order.setUserEmail(orderDto.getUserEmail());
            order.setDelivery(true);
            order.setAddress(orderDto.getAddress());
            order.setAddressSecondLine(orderDto.getAddressSecondLine());
            order.setCity(orderDto.getCity());
            order.setZipCode(orderDto.getZipCode());
            if(isCardPayment) {
                order.setPaymentMethod(PaymentMethod.CARD);
            } else {
                order.setPaymentMethod(PaymentMethod.CASH);
            }
        }
        order.setOrderStatus(OrderStatus.ZAAKCEPTOWANE);
        order.setDelivery(false);
        order.setTotalPrice(zero);
        if(orderDto.getOrderType().equals("PHONE")) {
            order.setOrderType(OrderType.PHONE);
        }
        else {
            order.setOrderType(OrderType.IN_PERSON);
        }
        return orderRepository.save(order);
    }

    @Override
    public Orders getOrderById(Long orderId) {
        Optional<Orders> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new OrderNotFoundException("Order not found");
        }
    }

    @Override
    @Transactional
    public void addMenuItemToOrder(Orders order, Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found"));

        Optional<OrderItems> existingOrderItem = order.getItems().stream()
                .filter(item -> item.getMenuItemId().equals(menuItemId))
                .findFirst();

        if (existingOrderItem.isPresent()) {
            OrderItems orderItem = existingOrderItem.get();
            orderItem.setQuantity(orderItem.getQuantity() + 1);
            updateTotalPriceOrderOnSite(order);
            orderItemRepository.save(orderItem);
        } else {
            OrderItems orderItem = new OrderItems(order, menuItemId, 1);
            orderItem.setOrders(order);
            orderItem.setMenuItemId(menuItemId);
            orderItem.setQuantity(1);
            order.addItem(orderItem);
            updateTotalPriceOrderOnSite(order);
            orderRepository.save(order);
        }
    }

    @Override
    public void deleteMenuItemFromOrder(Orders order, Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found"));

        Optional<OrderItems> existingOrderItem = order.getItems().stream()
                .filter(item -> item.getMenuItemId().equals(menuItemId))
                .findFirst();

        if(existingOrderItem.isPresent()) {
            OrderItems orderItem = existingOrderItem.get();
            orderItem.setQuantity(orderItem.getQuantity() - 1);
            if (orderItem.getQuantity() <= 0) {
                order.getItems().remove(orderItem);
                orderItemRepository.delete(orderItem);
            } else {
                orderItemRepository.save(orderItem);
            }
            updateTotalPriceOrderOnSite(order);
        } else {
            throw new MenuItemNotFoundException("Menu item not found in the order");
        }
    }

    @Override
    @Transactional
    public void updateTotalPriceOrderOnSite(Orders order){
        List<OrderItems> orderItems = orderItemRepository.findAllByOrders(order);


        BigDecimal totalPrice = orderItems.stream()

                .map(item -> {

                    Long menuItemID = item.getMenuItemId();

                    BigDecimal unitPrice = menuItemRepository.findById(menuItemID)
                            .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found"))
                            .getPrice();


                    BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());


                    return unitPrice.multiply(quantity);
                })

                .reduce(BigDecimal.ZERO, BigDecimal::add);


        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
    }


    @Override
    public void deleteOrderOnSite(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    @Override
    @Transactional
    public void acceptOrder(Long orderId) {
        Orders order = getOrderById(orderId);
        order.setOrderStatus(OrderStatus.ZAAKCEPTOWANE);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Orders order = getOrderById(orderId);
        order.setOrderStatus(OrderStatus.ANULOWANE);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void changeOrderStatusToInProgress(Long orderId) {
        Orders order = getOrderById(orderId);
        order.setOrderStatus(OrderStatus.W_TRAKCIE_PRZYGOTOWANIA);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void changeOrderStatusToDone(Long orderId) {
        Orders order = getOrderById(orderId);
        order.setOrderStatus(OrderStatus.ZREALIZOWANE);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void changeOrderStatusToInDelivery(Long orderId) {
        Orders order = getOrderById(orderId);
        order.setOrderStatus(OrderStatus.W_DRODZE);
    }

    private List<OrderItems> getMenuItemsFromCart(CartSession cartSession, Orders order) {

        List<Long> productIds = cartSession.getItems().stream()
                .map(CartItems::getMenuItemId)
                .collect(Collectors.toList());

        List<MenuItem> menuItems = menuItemRepository.findAllById(productIds);
        return menuItems.stream()
                .map(menuItem -> new OrderItems(order, menuItem.getId(), 1))
                .collect(Collectors.toList());
    }

//    private static String generateOrderNumber() {
//        int year = Year.now().getValue() % 100;
//
//        int randInt = ThreadLocalRandom.current().nextInt(1000);
//
//        return String.format("%02d%04d", year, randInt);
//    }

}
