package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order extends Cart {
    private Long id;

    private BigDecimal subtotal;

    private BigDecimal deliveryCost;

    private BigDecimal totalCost;

    private int totalQuantity;

    private String firstName;

    private String lastName;

    private String phone;

    private LocalDate deliveryDate;

    private String deliveryAddress;

    private PaymentMethod paymentMethod;

    public Order() {
    }

    public Order(Order other) {
        this.id = other.id;
        this.subtotal = other.subtotal;
        this.deliveryCost = other.deliveryCost;
        this.totalCost = other.totalCost;
        this.totalQuantity = other.totalQuantity;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.phone = other.phone;
        this.deliveryDate = other.deliveryDate;
        this.deliveryAddress = other.deliveryAddress;
        this.paymentMethod = other.paymentMethod;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public int getTotalQuantity() {
        return totalQuantity;
    }

    @Override
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
