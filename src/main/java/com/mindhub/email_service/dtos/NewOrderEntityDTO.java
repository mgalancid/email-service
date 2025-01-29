package com.mindhub.email_service.dtos;

import java.io.Serializable;
import java.util.List;

public class NewOrderEntityDTO implements Serializable {
    private String userEmail;
    private String status;
    private List<NewOrderItemEntityDTO> products;

    public NewOrderEntityDTO() {}

    public NewOrderEntityDTO(String userEmail, String status, List<NewOrderItemEntityDTO> products) {
        this.userEmail = userEmail;
        this.status = status;
        this.products = products;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getStatus() {
        return status;
    }

    public List<NewOrderItemEntityDTO> getProducts() {
        return products;
    }
}

