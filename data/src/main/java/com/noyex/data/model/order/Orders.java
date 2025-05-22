package com.noyex.data.model.order;

import com.noyex.data.enums.OrderStatus;
import com.noyex.data.enums.OrderType;
import com.noyex.data.enums.PaymentMethod;
import com.noyex.data.model.MenuItem;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderNumber;

    private String firstName;

    private String lastName;

    private String userEmail;

    private String phone;

    private String address;

    private String addressSecondLine;

    private String city;

    private String zipCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String comment;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private boolean delivery;

    public Orders() {
    }

    public Orders(Long id, String orderNumber, String firstName, String lastName, String userEmail, String phone, String address, String addressSecondLine, String city, String zipCode, LocalDateTime createdAt, String comment, BigDecimal totalPrice, OrderStatus orderStatus, OrderType orderType, List<OrderItems> items, PaymentMethod paymentMethod, boolean delivery) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.phone = phone;
        this.address = address;
        this.addressSecondLine = addressSecondLine;
        this.city = city;
        this.zipCode = zipCode;
        this.createdAt = createdAt;
        this.comment = comment;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.orderType = orderType;
        this.items = items;
        this.paymentMethod = paymentMethod;
        this.delivery = delivery;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressSecondLine() {
        return addressSecondLine;
    }

    public void setAddressSecondLine(String addressSecondLine) {
        this.addressSecondLine = addressSecondLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }


    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public List<OrderItems> getItems() {
        return items;
    }

    public void setItems(List<OrderItems> items) {
        this.items = items;
    }

    public void addItem(OrderItems item) {
        items.add(item);
        item.setOrders(this);
    }
}
