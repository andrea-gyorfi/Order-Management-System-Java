package com.example.assignment3.model;

/**
 * This class represent a bill.
 * @param billId is the bill's id.
 * @param clientName is the bill's client name.
 * @param amount is the total amount of the bill.
 */
public record Bill(int billId, String clientName, double amount) {
}
