package com.example.assignment3.bll.validators;

import com.example.assignment3.model.Product;

/**
 *  This class validates the product's stock.
 */
public class ProductStockValidator implements Validator<Product>{
    private static final int MIN_STOCK=1;

    /**
     * This method checks the product's stock.
     * @param product is the product for which will check if the stock is >= 1.
     */
    public void validate(Product product) {
        if (product.getStock() < MIN_STOCK) {
            throw new IllegalArgumentException("Product stock can't be less than 1!");
        }
    }
}
