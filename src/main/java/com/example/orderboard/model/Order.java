package com.example.orderboard.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class Order {
    private final int userId;
    private final double quantity;
    private final double price;
    private final OrderType type;

    public Order(final int userId, final double quantity, final double price, final OrderType type) {
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public OrderType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, quantity, price, type);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        Order order = (Order) obj;
        return Objects.equals(userId, order.userId)
                && Objects.equals(quantity, order.quantity)
                && Objects.equals(price, order.price)
                && Objects.equals(type, order.type);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", userId)
                .add("quantity", quantity)
                .add("price", price)
                .add("type", type)
                .toString();
    }
}