package com.example.orderboard;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import com.example.orderboard.model.Order;
import com.example.orderboard.model.OrderSummary;
import com.example.orderboard.model.OrderType;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class OrderServiceImpl implements OrderService {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock writeLock = lock.writeLock();
    private final Lock readLock = lock.readLock();

    private final OrderValidator validator;
    private final Map<OrderType, Set<Order>> orders;
    
    public OrderServiceImpl(final OrderValidator validator) {
        this.validator = validator;

        this.orders = Maps.newHashMap();
        for (OrderType type : OrderType.values()) {
            this.orders.put(type, Sets.newHashSet());
        }
    }
    
    @Override
    public void registerOrder(final Order order) {
        validator.validate(order);

        try {
            writeLock.lock();
            orders.get(order.getType()).add(order);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void cancelOrder(final Order order) {
        validator.validate(order);

        try {
            writeLock.lock();
            orders.get(order.getType()).remove(order);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Set<Order> getOrders(final OrderType type) {
        try {
            readLock.lock();
            return newHashSet(orders.get(type));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<OrderSummary> getOrdersSummary(final OrderType type) {
        //system could use BigDecimal for processing if exact precision is important for displaying board
        Map<Double, Double> quantityByPrice = getOrders(type).stream()
                .collect(Collectors.groupingBy(
                        Order::getPrice, HashMap::new, Collectors.reducing(0.0, Order::getQuantity, Double::sum)));

        return quantityByPrice.entrySet().stream()
                .map(e -> new OrderSummary(e.getValue(), e.getKey()))
                .sorted(type.getComparator())
                .collect(Collectors.toList());
    }
}