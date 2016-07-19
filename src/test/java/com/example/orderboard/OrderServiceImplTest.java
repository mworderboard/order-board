package com.example.orderboard;

import static com.example.orderboard.model.OrderType.BUY;
import static com.example.orderboard.model.OrderType.SELL;
import static com.example.orderboard.util.TestUtils.FIRST_PRICE;
import static com.example.orderboard.util.TestUtils.FIRST_QUANTITY;
import static com.example.orderboard.util.TestUtils.SECOND_PRICE;
import static com.example.orderboard.util.TestUtils.SECOND_QUANTITY;
import static com.example.orderboard.util.TestUtils.USER_ID;
import static com.example.orderboard.util.TestUtils.createOrder;
import static com.example.orderboard.util.TestUtils.createSummary;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.example.orderboard.model.Order;
import com.example.orderboard.model.OrderSummary;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @Mock
    private OrderValidator orderValidator;
    
    @InjectMocks
    private OrderServiceImpl underTest;
    
    @Test
    public void testThatRegisterOrderAddsOrderToStore() {
        assertThat(underTest.getOrders(BUY), empty());

        Order firstOrder = createOrder(USER_ID, FIRST_QUANTITY, FIRST_PRICE, BUY);
        underTest.registerOrder(firstOrder);
        assertThat(underTest.getOrders(BUY), containsInAnyOrder(firstOrder));

        Order secondOrder = createOrder(USER_ID, SECOND_QUANTITY, SECOND_PRICE, BUY);
        underTest.registerOrder(secondOrder);
        assertThat(underTest.getOrders(BUY), containsInAnyOrder(firstOrder, secondOrder));
    }
    
    @Test
    public void testThatCancellOrderRemovesOrderFromStore() {
        Order firstOrder = createOrder(USER_ID, FIRST_QUANTITY, FIRST_PRICE, SELL);
        Order secondOrder = createOrder(USER_ID, SECOND_QUANTITY, SECOND_PRICE, SELL);
        underTest.registerOrder(firstOrder);
        underTest.registerOrder(secondOrder);
        assertThat(underTest.getOrders(SELL), containsInAnyOrder(firstOrder, secondOrder));
        
        underTest.cancelOrder(firstOrder);
        assertThat(underTest.getOrders(SELL), containsInAnyOrder(secondOrder));
        
        underTest.cancelOrder(secondOrder);
        assertThat(underTest.getOrders(SELL), empty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThatOrderIsNotRegisteredIfValidationFails() {
        Order order = createOrder(USER_ID, FIRST_QUANTITY, FIRST_PRICE, BUY);
        doThrow(IllegalArgumentException.class).when(orderValidator).validate(order);
        
        try {
            underTest.registerOrder(order);
        } finally {
            assertThat(underTest.getOrders(BUY), empty());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThatOrderIsNotCancelledIfValidationFails() {
        Order order = createOrder(USER_ID, FIRST_QUANTITY, FIRST_PRICE, BUY);
        doNothing().doThrow(IllegalArgumentException.class).when(orderValidator).validate(order);
        
        try {
            underTest.registerOrder(order);
            assertThat(underTest.getOrders(BUY), contains(order));
            
            underTest.cancelOrder(order);
        } finally {
            assertThat(underTest.getOrders(BUY), contains(order));
        }
    }
    
    @Test
    public void testThatEachOrderSideIsProcessedSeparately() {
        Order buyOrder = createOrder(USER_ID, FIRST_QUANTITY, FIRST_PRICE, BUY);
        underTest.registerOrder(buyOrder);
        assertThat(underTest.getOrders(BUY), containsInAnyOrder(buyOrder));
        assertThat(underTest.getOrders(SELL), empty());
        
        Order sellOrder = createOrder(USER_ID, FIRST_QUANTITY, FIRST_PRICE, SELL);
        underTest.registerOrder(sellOrder);
        assertThat(underTest.getOrders(BUY), containsInAnyOrder(buyOrder));
        assertThat(underTest.getOrders(SELL), containsInAnyOrder(sellOrder));

        underTest.cancelOrder(buyOrder);
        assertThat(underTest.getOrders(BUY), empty());
        assertThat(underTest.getOrders(SELL), containsInAnyOrder(sellOrder));

        underTest.cancelOrder(sellOrder);
        assertThat(underTest.getOrders(BUY), empty());
        assertThat(underTest.getOrders(SELL), empty());
    }

    @Test
    public void testThatGetOrdersSummaryReturnsBuyOrdersSummaryInCorrectSequence() {
        Order firstOrder = createOrder(USER_ID, FIRST_QUANTITY, FIRST_PRICE, BUY);
        OrderSummary firstSummary = createSummary(FIRST_QUANTITY, FIRST_PRICE);
        underTest.registerOrder(firstOrder);
        assertThat(underTest.getOrdersSummary(BUY), contains(firstSummary));

        Order secondOrder = createOrder(USER_ID, FIRST_QUANTITY, SECOND_PRICE, BUY);
        OrderSummary secondSummary = createSummary(FIRST_QUANTITY, SECOND_PRICE);
        underTest.registerOrder(secondOrder);
        assertThat(underTest.getOrdersSummary(BUY), contains(secondSummary, firstSummary));
    }

    @Test
    public void testThatGetOrdersSummaryReturnsSellOrdersSummaryInCorrectSequence() {
        Order firstOrder = createOrder(USER_ID, FIRST_QUANTITY, FIRST_PRICE, SELL);
        OrderSummary firstSummary = createSummary(FIRST_QUANTITY, FIRST_PRICE);
        underTest.registerOrder(firstOrder);
        assertThat(underTest.getOrdersSummary(SELL), containsInAnyOrder(firstSummary));

        Order secondOrder = createOrder(USER_ID, FIRST_QUANTITY, SECOND_PRICE, SELL);
        OrderSummary secondSummary = createSummary(FIRST_QUANTITY, SECOND_PRICE);
        underTest.registerOrder(secondOrder);
        assertThat(underTest.getOrdersSummary(SELL), containsInAnyOrder(firstSummary, secondSummary));
    }

    @Test
    public void testThatGetOrdersSummaryMergesOrdersWithSamePrice() {
        underTest.registerOrder(createOrder(USER_ID, 3.5, FIRST_PRICE, BUY));
        assertThat(underTest.getOrdersSummary(BUY), contains(createSummary(3.5, FIRST_PRICE)));

        underTest.registerOrder(createOrder(USER_ID, 1, SECOND_PRICE, BUY));
        assertThat(underTest.getOrdersSummary(BUY), contains(createSummary(1, SECOND_PRICE), createSummary(3.5, FIRST_PRICE)));

        underTest.registerOrder(createOrder(USER_ID, 2, FIRST_PRICE, BUY));
        assertThat(underTest.getOrdersSummary(BUY), contains(createSummary(1, SECOND_PRICE), createSummary(5.5, FIRST_PRICE)));

        underTest.registerOrder(createOrder(USER_ID, 0.3, SECOND_PRICE, BUY));
        assertThat(underTest.getOrdersSummary(BUY), contains(createSummary(1.3, SECOND_PRICE), createSummary(5.5, FIRST_PRICE)));
    }
}