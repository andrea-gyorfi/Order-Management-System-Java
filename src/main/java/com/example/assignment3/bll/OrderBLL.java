package com.example.assignment3.bll;

import com.example.assignment3.bll.validators.OrderQuantityValidator;
import com.example.assignment3.bll.validators.Validator;
import com.example.assignment3.dao.OrderDAO;
import com.example.assignment3.model.Orders;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class performs the CRUD operations related to orders.
 */
public class OrderBLL {
    private List<Validator<Orders>> validators;

    public OrderBLL() {
        validators = new ArrayList<>();
        validators.add(new OrderQuantityValidator());
    }

    /**
     * This method finds an order based on the id.
     * @param id is the order's id it searches for.
     * @return the order with the given id from the database.
     */
    public Orders findOrderById(int id) {
        Orders order = new OrderDAO().findById(id);
        if (order == null) {
            throw new NoSuchElementException("The order with id =" + id + " was not found!");
        }
        return order;
    }

    /**
     * This method returns a list of all the orders from the database.
     * @return the list of the orders.
     */
    public List<Orders> findAll() {
        List<Orders> orders = new OrderDAO().findAll();
        if (orders.isEmpty()) {
            throw new NoSuchElementException("No orders found!");
        }
        return orders;
    }

    /**
     * This method inserts an order in the database.
     * @param order is the order which will be inserted.
     */
    public void insertOrder(Orders order) {
        validators.forEach(v -> v.validate(order));
        Orders or = new OrderDAO().insert(order);
        if (or == null) {
            throw new IllegalStateException("The order with id = " + order.getId() + "could not be inserted!");
        }
    }

}
