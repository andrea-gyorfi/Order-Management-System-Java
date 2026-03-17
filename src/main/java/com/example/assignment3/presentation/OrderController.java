package com.example.assignment3.presentation;

import com.example.assignment3.bll.BillBLL;
import com.example.assignment3.bll.ClientBLL;
import com.example.assignment3.bll.OrderBLL;
import com.example.assignment3.bll.ProductBLL;
import com.example.assignment3.model.Bill;
import com.example.assignment3.model.Client;
import com.example.assignment3.model.Orders;
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
 *  This class manages order-related GUI interactions.
 */
public class OrderController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableView<Orders> orderTable;
    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableView<Bill> billTable;
    @FXML
    private VBox findById;
    @FXML
    private VBox makeOrder;
    @FXML
    private VBox products;
    @FXML
    private VBox clients;
    @FXML
    private VBox bills;
    @FXML
    private VBox billsText;
    @FXML
    private TextField id1;
    @FXML
    private TextField quantity1;

    private OrderBLL orderBll = new OrderBLL();
    private ProductBLL productBll = new ProductBLL();
    private ClientBLL clientBll = new ClientBLL();
    private BillBLL billBll = new BillBLL();

    /**
     *  This method will handle the action to view all the orders from the database.
     *  It shows only the parts related to this action and loads all the orders into the table.
     */
    @FXML
    private void onViewOrdersClick() {
        findById.setVisible(false);
        makeOrder.setVisible(false);
        products.setVisible(false);
        clients.setVisible(false);
        bills.setVisible(false);
        billsText.setVisible(false);
        try{
            List<Orders> orderList = orderBll.findAll();
            TableViewUtility.putInTable(orderTable, orderList);
        }catch (NoSuchElementException e){
            showAlert("No orders found!");
        }
    }

    /**
     *  This method shows only the parts related to finding an order by ID.
     */
    @FXML
    private void onSelectOrderClick() {
        findById.setVisible(true);
        makeOrder.setVisible(false);
        products.setVisible(false);
        clients.setVisible(false);
        bills.setVisible(false);
        billsText.setVisible(false);
    }

    /**
     *  This method will handle the action to find an order by ID.
     *  It shows only the parts related to this action and displays in the table the found order (if found).
     *  Shows alerts for invalid input or if order not found.
     */
    @FXML
    private void onSaveSelectOrderClick() {
        findById.setVisible(true);
        makeOrder.setVisible(false);
        products.setVisible(false);
        clients.setVisible(false);
        bills.setVisible(false);
        billsText.setVisible(false);

        try{
            int idOrder = Integer.parseInt(id1.getText());
            Orders order = orderBll.findOrderById(idOrder);
            List<Orders> orderList = new ArrayList<>();
            orderList.add(order);
            TableViewUtility.putInTable(orderTable, orderList);
        }catch(NumberFormatException e){
            showAlert("Invalid input!");
        }catch(NoSuchElementException e){
            showAlert("No order found!");
        }

        id1.clear();
    }

    /**
     * This method shows only the parts related to the insertion of an order.
     * Displays a table with the clients, and a table with the products.
     */
    @FXML
    private void onMakeOrderClick() {
        findById.setVisible(false);
        makeOrder.setVisible(true);
        products.setVisible(true);
        clients.setVisible(true);
        bills.setVisible(false);
        billsText.setVisible(false);

        try{
            List<Product> productList = productBll.findAll();
            TableViewUtility.putInTable(productTable, productList);
        }catch (NoSuchElementException e){
            showAlert("No products found!");
        }

        try{
            List<Client> clientList = clientBll.findAll();
            TableViewUtility.putInTable(clientTable, clientList);
        }catch (NoSuchElementException e){
            showAlert("No clients found!");
        }
    }

    /**
     *  This method will handle the action to insert an order.
     *  It shows only the parts related to this action and saves the order.
     *  Shows alerts for invalid input or if it failed, and also for under-stock.
     */
    @FXML
    private void onSaveMakeOrderClick() {
        findById.setVisible(false);
        makeOrder.setVisible(true);
        products.setVisible(true);
        clients.setVisible(true);
        bills.setVisible(false);
        billsText.setVisible(false);

        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        int idClient = selectedClient.getId();
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        int idProduct = selectedProduct.getId();
        String quantityOrder = quantity1.getText();

        if(selectedClient == null || selectedProduct == null || quantityOrder.isEmpty()){
            showAlert("Invalid input!");
            return;
        }

        try{
            int quantity = Integer.parseInt(quantityOrder);
            if(quantity>selectedProduct.getStock())
            {
                showAlert("Under-stock!");
                return;
            }
            Orders order = new Orders(idClient,idProduct,quantity);
            orderBll.insertOrder(order);
            selectedProduct.setStock(selectedProduct.getStock() - quantity);
            productBll.updateProduct(selectedProduct);
            Bill bill = new Bill(0,selectedClient.getName(),quantity*selectedProduct.getPrice());
            billBll.insert(bill);
        }catch(NumberFormatException e){
            showAlert("Invalid input!");
        }catch(IllegalStateException e){
            showAlert("Couldn't insert order!");
        }

        quantity1.clear();
    }

    /**
     *  This method will handle the action to view all the bills from the database.
     *  It shows only the parts related to this action and loads all the bills into the table.
     */
    @FXML
    private void onViewBillsClick() {
        findById.setVisible(false);
        makeOrder.setVisible(false);
        products.setVisible(false);
        clients.setVisible(false);
        bills.setVisible(true);
        billsText.setVisible(true);

        try{
            List<Bill> billList = billBll.findAll();
            TableViewUtility.putInTable(billTable, billList);
        }catch (NoSuchElementException e)
        {
            showAlert("No bills found!");
        }
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
