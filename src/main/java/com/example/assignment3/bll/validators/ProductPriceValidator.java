package com.example.assignment3.bll.validators;

import com.example.assignment3.model.Product;

/**
 *  This class validates the product's price.
 */
public class ProductPriceValidator implements Validator<Product> {
    private static final double PRODUCT_PRICE = 1;

    /**
     * This method check the product's price.
     * @param product is the product for which will check if the price is >= 1.
     */
    public void validate(Product product) {
        if(product.getPrice() < PRODUCT_PRICE)
        {
            throw new IllegalArgumentException("Product price can't be less than 1!");
        }
    }
}
