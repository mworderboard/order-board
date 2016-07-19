package com.example.orderboard;

import com.example.orderboard.model.Order;

public interface OrderValidator {

    /**
     * Validates provided order.
     *
     * @param order the order to validate.
     */
    void validate(Order order);
}