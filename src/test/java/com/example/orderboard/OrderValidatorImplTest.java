package com.example.orderboard;

import static com.example.orderboard.model.OrderType.BUY;
import static com.example.orderboard.util.TestUtils.FIRST_PRICE;
import static com.example.orderboard.util.TestUtils.FIRST_QUANTITY;
import static com.example.orderboard.util.TestUtils.USER_ID;
import static com.example.orderboard.util.TestUtils.createOrder;

import org.junit.Test;

public class OrderValidatorImplTest {

    private OrderValidator underTest = new OrderValidatorImpl();

    @Test
    public void testThatValidateDoesNothingWhenOrderIsCorrect() {
        underTest.validate(createOrder(USER_ID, FIRST_QUANTITY, FIRST_PRICE, BUY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatValidateThrowsExceptionWhenUserIdIsInvalid() {
        underTest.validate(createOrder(-1, FIRST_QUANTITY, FIRST_PRICE, BUY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatValidateThrowsExceptionWhenQuantityIsInvalid() {
        underTest.validate(createOrder(USER_ID, -1, FIRST_PRICE, BUY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatValidateThrowsExceptionWhenPriceIsInvalid() {
        underTest.validate(createOrder(USER_ID, FIRST_QUANTITY, 0, BUY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatValidateThrowsExceptionWhenTypeIsNotProvided() {
        underTest.validate(createOrder(USER_ID, FIRST_QUANTITY, FIRST_PRICE, null));
    }
}
