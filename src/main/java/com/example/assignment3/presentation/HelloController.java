package com.example.assignment3.presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/**
 *  This class handles the interaction with the main window.
 */
public class HelloController {

    /**
     * This method handles the action when the "Clients" button is clicked.
     * Displays a new window related to clients.
     */
    @FXML
    private void onClientsClick() {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assignment3/View/clients-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Clients!");
            Scene scene = new Scene(loader.load(), 720, 750);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  This method handles the action when the "Orders" button is clicked.
     *  Displays a new window related to orders.
     */
    @FXML
    private void onOrdersClick() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assignment3/View/orders-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Orders!");
            Scene scene = new Scene(loader.load(), 780, 750);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  This method handles the action when the "Products" button is clicked.
     *  Displays a new window related to products.
     */
    @FXML
    private void onProductsClick() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assignment3/View/products-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Products!");
            Scene scene = new Scene(loader.load(), 600, 650);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}