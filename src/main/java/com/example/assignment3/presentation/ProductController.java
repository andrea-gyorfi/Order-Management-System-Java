package com.example.assignment3.presentation;

import com.example.assignment3.bll.ProductBLL;
import com.example.assignment3.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *  This class manages product-related GUI interactions.
 */
public class ProductController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private VBox findById;
    @FXML
    private VBox insert;
    @FXML
    private VBox update;
    @FXML
    private VBox deleteOp;
    @FXML
    private TextField id1;
    @FXML
    private TextField id2;
    @FXML
    private TextField id3;
    @FXML
    private TextField name1;
    @FXML
    private TextField price1;
    @FXML
    private TextField stock1;
    @FXML
    private TextField name2;
    @FXML
    private TextField price2;
    @FXML
    private TextField stock2;

    private ProductBLL productBll = new ProductBLL();

    /**
     *  This method will handle the action to view all the products from the database.
     *  It shows only the parts related to this action and loads all the products into the table.
     */
    @FXML
    private void onViewProductsClick() {
        findById.setVisible(false);
        insert.setVisible(false);
        update.setVisible(false);
        deleteOp.setVisible(false);
        try{
            List<Product> productList = productBll.findAll();
            TableViewUtility.putInTable(productTable, productList);
        }catch (NoSuchElementException e){
            showAlert("No products found!");
        }
    }

    /**
     * This method shows only the parts related to finding a product by ID.
     */
    @FXML
    private void onSelectProductClick() {
        findById.setVisible(true);
        insert.setVisible(false);
        update.setVisible(false);
        deleteOp.setVisible(false);
    }

    /**
     *  This method will handle the action to find a product by ID.
     *  It shows only the parts related to this action and displays in the table the found product (if found).
     *  Shows alerts for invalid input or if product not found.
     */
    @FXML
    private void onSaveSelectProductClick() {
        findById.setVisible(true);
        insert.setVisible(false);
        update.setVisible(false);
        deleteOp.setVisible(false);
        try{
            int idProduct = Integer.parseInt(id1.getText());
            Product product = productBll.findProductById(idProduct);
            List<Product> productList = new ArrayList<>();
            productList.add(product);
            TableViewUtility.putInTable(productTable, productList);
        }catch(NumberFormatException e){
            showAlert("Invalid input!");
        }catch(NoSuchElementException e){
            showAlert("No product found!");
        }

        id1.clear();
    }

    /**
     *  This method shows only the parts related to the insertion of a product.
     */
    @FXML
    private void onInsertProductClick() {
        findById.setVisible(false);
        insert.setVisible(true);
        update.setVisible(false);
        deleteOp.setVisible(false);
    }


    /**
     *  This method will handle the action to insert a product.
     *  It shows only the parts related to this action and saves the product.
     *  Shows alerts for invalid input or if it failed.
     */
    @FXML
    private void onSaveInsertProductClick() {
        findById.setVisible(false);
        insert.setVisible(true);
        update.setVisible(false);
        deleteOp.setVisible(false);

        try{
            String nameProduct = name1.getText();
            double priceProduct = Double.parseDouble(price1.getText());
            int stockProduct = Integer.parseInt(stock1.getText());
            Product product = new Product(nameProduct, priceProduct, stockProduct);
            productBll.insertProduct(product);
        }catch(NumberFormatException e){
            showAlert("Invalid input!");
        }catch(IllegalArgumentException e){
            showAlert("Invalid input!");
        }catch(IllegalStateException e){
            showAlert("Couldn't insert product!");
        }

        name1.clear();
        price1.clear();
        stock1.clear();
    }

    /**
     *  This method shows only the parts related to updating a product.
     */
    @FXML
    private void onUpdateProductClick() {
        findById.setVisible(false);
        insert.setVisible(false);
        update.setVisible(true);
        deleteOp.setVisible(false);
    }

    /**
     *  This method will handle the action to update a product.
     *  It shows only the parts related to this action and saves the updates.
     *  Fields left empty won't be updated.
     *  Shows alerts for invalid input or product not found.
     */
    @FXML
    private void onSaveUpdateProductClick() {
        findById.setVisible(false);
        insert.setVisible(false);
        update.setVisible(true);
        deleteOp.setVisible(false);
        try{
            int idProduct = Integer.parseInt(id2.getText());
            String nameProduct = name2.getText();
            String priceProduct = price2.getText();
            String stockProduct = stock2.getText();

            Product product = productBll.findProductById(idProduct);

            if(!nameProduct.isEmpty()){
                product.setName(nameProduct);
            }
            if(!priceProduct.isEmpty()){
                product.setPrice(Double.parseDouble(priceProduct));
            }
            if(!stockProduct.isEmpty()){
                product.setStock(Integer.parseInt(stockProduct));
            }
            productBll.updateProduct(product);
        } catch (IllegalArgumentException e){
            showAlert("Invalid input!");
        }catch (NoSuchElementException e){
            showAlert("No product found!");
        }

        id2.clear();
        name2.clear();
        price2.clear();
        stock2.clear();
    }

    /**
     *  This method shows only the parts related to deleting a product.
     */
    @FXML
    private void onDeleteProductClick() {
        findById.setVisible(false);
        insert.setVisible(false);
        update.setVisible(false);
        deleteOp.setVisible(true);
    }

    /**
     *  This method will handle the action to delete a product.
     *  Deletes a product based on ID.
     *  Shows alerts for invalid input or product not found.
     */
    @FXML
    private void onSaveDeleteProductClick() {
        findById.setVisible(false);
        insert.setVisible(false);
        update.setVisible(false);
        deleteOp.setVisible(true);

        try{
            int idProduct = Integer.parseInt(id3.getText());
            Product product = productBll.findProductById(idProduct);
            productBll.deleteProduct(product);
        }catch (NoSuchElementException e)
        {
            showAlert("No product found!");
        } catch (NumberFormatException e) {
            showAlert("Invalid input!");
        }

        id3.clear();
    }

    /**
     * Shows an alert with the given message.
     * @param mess is the message to be displayed.
     */
    private void showAlert(String mess) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mess);
        alert.showAndWait();
    }
}
