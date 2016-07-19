package com.example.orderboard;

import static com.google.common.base.Preconditions.checkArgument;

import com.example.orderboard.model.Order;

public class OrderValidatorImpl implements OrderValidator {

    /**
     * Validates provided order.
     *
     * @param order the order to validate.
     */
    @Override
    public void validate(Order order) {
        checkArgument(order != null, "Cannot process empty request");
        checkArgument(order.getUserId() > 0, "Cannot process order - userId is invalid: " + order.getUserId());
        checkArgument(order.getQuantity() > 0, "Cannot process order - quantity is invalid: " + order.getQuantity());
        checkArgument(order.getPrice() > 0, "Cannot process order - price is invalid: " + order.getPrice());
        checkArgument(order.getType() != null, "Cannot process order - type is not provided");
    }
}
