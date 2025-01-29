package com.mindhub.email_service.dtos;

import java.io.Serializable;
import java.util.List;

public class OrderEntityDTO implements Serializable {
    private Long id;
    private Long userId;
    private String status;
    private List<OrderItemDTO> products;

    public OrderEntityDTO() {

    }

    public OrderEntityDTO(Long id, Long userId, String status, List<OrderItemDTO> products) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getStatus() { return status; }

    public List<OrderItemDTO> getProducts() {
        return products;
    }

}
