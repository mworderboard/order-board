package com.example.orderboard;

import java.util.List;
import java.util.Set;

import com.example.orderboard.model.Order;
import com.example.orderboard.model.OrderSummary;
import com.example.orderboard.model.OrderType;

public interface OrderService {

    /**
     * Adds an order to order board.
     * 
     * @param order an order to add.
     */
    void registerOrder(Order order);

    /**
     * Removes order from order board.
     * 
     * @param order an order to remove.
     */
    void cancelOrder(Order order);

    /**
     * Returns all orders of appropriate type.
     * 
     * @param type the type of orders.
     */
    Set<Order> getOrders(OrderType type);

    /**
     * Returns orders summary of appropriate type.
     * 
     * @param type the type of orders.
     */
    List<OrderSummary> getOrdersSummary(OrderType type);
}