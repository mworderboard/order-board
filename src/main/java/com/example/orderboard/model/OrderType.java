package com.example.orderboard.model;

import java.util.Comparator;

import com.google.common.primitives.Doubles;

public enum OrderType {
    BUY((os1, os2) -> Doubles.compare(os2.getPrice(), os1.getPrice())),
    SELL((os1, os2) -> Doubles.compare(os1.getPrice(), os2.getPrice()));

    private final Comparator<OrderSummary> summaryComparator;

    private OrderType(final Comparator<OrderSummary> summaryComparator) {
        this.summaryComparator = summaryComparator;
    }

    public Comparator<OrderSummary> getComparator() {
        return this.summaryComparator;
    }
}