package com.example.orderboard.util;

import com.example.orderboard.model.Order;
import com.example.orderboard.model.OrderSummary;
import com.example.orderboard.model.OrderType;

public final class TestUtils {
    public static final int USER_ID = 101;
    public static final double FIRST_QUANTITY = 202;
    public static final double SECOND_QUANTITY = 202;
    public static final double FIRST_PRICE = 303;
    public static final double SECOND_PRICE = 404;

    private TestUtils() {
    }

    public static Order createOrder(final int userId, final double quantity, final double price, final OrderType type) {
        return new Order(userId, quantity, price, type);
    }

    public static OrderSummary createSummary(final double quantity, final double price) {
        return new OrderSummary(quantity, price);
    }
}