package com.mindhub.email_service.dtos;

import java.io.Serializable;
import java.util.List;

public class OrderConfirmationEmailDTO implements Serializable {
    private Long orderId;
    private String userEmail;
    private String status;
    private List<OrderItemDTO> products;

    public OrderConfirmationEmailDTO() {

    }

    public OrderConfirmationEmailDTO(Long orderId,
                                     String userEmail,
                                     String status,
                                     List<OrderItemDTO> products) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.status = status;
        this.products = products;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderItemDTO> getProducts() {
        return products;
    }
}

