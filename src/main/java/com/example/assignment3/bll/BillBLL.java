package com.example.assignment3.bll;

import com.example.assignment3.dao.BillDAO;
import com.example.assignment3.model.Bill;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class performs the insertion of the bills and shows the list of all the bills.
 */
public class BillBLL {

    /**
     * This method inserts a bill in the database.
     * @param bill is the bill which will be inserted.
     */
    public void insert(Bill bill) {
        Bill bl = new BillDAO().insertBill(bill);
        if (bl == null) {
            throw new IllegalStateException("Can't insert bill");
        }
    }

    /**
     * This method returns a list of all the bills sorted in ascending order by amount.
     * @return a sorted list of the bills from the database.
     */
    public List<Bill> findAll() {
        List<Bill> bills = new BillDAO().findAll()
                .stream()
                .sorted((b1, b2) -> Double.compare(b1.amount(), b2.amount()))
                .toList();
        if (bills.isEmpty()) {
            throw new NoSuchElementException("No bills found!");
        }
        return bills;
    }
}
