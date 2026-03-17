package com.example.assignment3.bll.validators;

import com.example.assignment3.model.Orders;

/**
 *  This class validates an order's quantity.
 */
public class OrderQuantityValidator implements Validator<Orders> {
    private static final int MIN_QUANTITY=1;

    /**
     * This method check an order's quantity.
     * @param order is the order for which will check if the quantity is >= 1.
     */
    public void validate(Orders order) {
        if (order.getQuantity() < MIN_QUANTITY) {
            throw new IllegalArgumentException("Order quantity can't be less than 1!");
        }
    }
}
