package com.example.assignment3.bll;

import com.example.assignment3.bll.validators.ProductNameValidator;
import com.example.assignment3.bll.validators.ProductPriceValidator;
import com.example.assignment3.bll.validators.ProductStockValidator;
import com.example.assignment3.bll.validators.Validator;
import com.example.assignment3.dao.ProductDAO;
import com.example.assignment3.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class performs the CRUD operations related to products.
 */
public class ProductBLL {
    private List<Validator<Product>> validators;

    public ProductBLL() {
        validators = new ArrayList<>();
        validators.add(new ProductPriceValidator());
        validators.add(new ProductNameValidator());
        validators.add(new ProductStockValidator());
    }

    /**
     * This method finds a product based on the id.
     * @param id is the product's id it searches for
     * @return the product with the given id from the database.
     */
    public Product findProductById(int id) {
        Product product = new ProductDAO().findById(id);
        if (product == null) {
            throw new NoSuchElementException("The products with id =" + id + " was not found!");
        }
        return product;
    }

    /**
     * The method returns a list of all the products from the database.
     * @return the list of the products.
     */
    public List<Product> findAll() {
        List<Product> products = new ProductDAO().findAll();
        if (products.isEmpty()) {
            throw new NoSuchElementException("No products found!");
        }
        return products;
    }

    /**
     * This method inserts a product in the database.
     * @param product is the product which will be inserted.
     */
    public void insertProduct(Product product) {
        validators.forEach(v -> v.validate(product));
        Product pr = new ProductDAO().insert(product);
        if (pr == null) {
            throw new IllegalStateException("The product with id =" + product.getId() + " could not be inserted!");
        }
    }

    /**
     * This method updates a product from the database.
     * @param product is the product to be updated.
     */
    public void updateProduct(Product product) {
        validators.forEach(v -> v.validate(product));
        Product pr = new ProductDAO().update(product);
        if (pr == null) {
            throw new NoSuchElementException("The product with id =" + product.getId() + " could not be updated!");
        }
    }

    /**
     * This method deletes a product from the database.
     * @param product is the product to be deleted.
     */
    public void deleteProduct(Product product) {
        Product pr = new ProductDAO().delete(product);
        if (pr == null) {
            throw new NoSuchElementException("The product with id =" + product.getId() + " could not be deleted!");
        }
    }
}
