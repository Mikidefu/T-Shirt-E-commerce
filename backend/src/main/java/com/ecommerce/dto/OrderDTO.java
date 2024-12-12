package com.ecommerce.dto;


import com.ecommerce.model.Order;

import java.util.List;

public class OrderDTO {

    private Long id;
    private Long user_id;
    private List<CartItemDTO> products;
    private double totalPrice;
    private Order.Status status;
}
