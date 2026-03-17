package com.example.assignment3.bll.validators;

import com.example.assignment3.model.Product;

/**
 *  This class validates the product's name.
 */
public class ProductNameValidator implements Validator<Product>{
    private static final int NAME_LENGTH = 3;

    /**
     * This method checks the product's name.
     * @param product is the product for which will check if the name has a length >= 3.
     */
    public void validate(Product product) {
        String name = product.getName();
        if (name == null || name.length() < NAME_LENGTH) {
            throw new IllegalArgumentException("Name is not valid!");
        }
    }
}
