package com.example.orderboard.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class OrderSummary {
    private final double price;
    private double quantity;

    public OrderSummary(final double quantity, final double price) {
        this.quantity = quantity;
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, price);
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

        OrderSummary orderSummary = (OrderSummary) obj;
        return Objects.equals(quantity, orderSummary.quantity)
                && Objects.equals(price, orderSummary.price);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("quantity", quantity)
                .add("price", price)
                .toString();
    }
}