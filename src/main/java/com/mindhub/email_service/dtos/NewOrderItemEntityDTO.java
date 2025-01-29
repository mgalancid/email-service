package com.mindhub.email_service.dtos;

import java.io.Serializable;

public class NewOrderItemEntityDTO implements Serializable {
    private Long productId;
    private Integer quantity;

    public NewOrderItemEntityDTO() {}

    public NewOrderItemEntityDTO(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}

