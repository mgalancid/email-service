package com.mindhub.email_service.dtos;

import java.io.Serializable;

public class OrderItemDTO implements Serializable {
    private Long id;
    private Long productId;
    private Integer quantity;

    public OrderItemDTO() {

    }

    public OrderItemDTO(Long id, Long productId, Integer quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
