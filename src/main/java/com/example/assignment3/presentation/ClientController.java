package com.example.assignment3.presentation;

import com.example.assignment3.bll.ClientBLL;
import com.example.assignment3.model.Client;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

/**
 *  This class manages client-related GUI interactions.
 */
public class ClientController {

    @FXML
    private TableView<Client> clientTable;
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
    private TextField address1;
    @FXML
    private TextField email1;
    @FXML
    private TextField age1;
    @FXML
    private TextField phone1;
    @FXML
    private TextField name2;
    @FXML
    private TextField address2;
    @FXML
    private TextField email2;
    @FXML
    private TextField age2;
    @FXML
    private TextField phone2;

    private ClientBLL clientBLL = new ClientBLL();

    /**
     *  This method will handle the action to view all the clients from the database.
     *  It shows only the parts related to this action and loads all the clients into the table.
     */
    @FXML
    private void onViewClientsClick() {
        findById.setVisible(false);
        insert.setVisible(false);
        update.setVisible(false);
        deleteOp.setVisible(false);
        try{
            List<Client> clientList = clientBLL.findAll();
            TableViewUtility.putInTable(clientTable, clientList);
        }catch (NoSuchElementException e){
            showAlert("No clients found!");
        }
    }

    /**
     *  This method shows only the parts related to finding a client by ID.
     */
    @FXML
    private void onSelectClientClick() {
        findById.setVisible(true);
        insert.setVisible(false);
        update.setVisible(false);
        deleteOp.setVisible(false);
    }

    /**
     *  This method will handle the action to find a client by ID.
     *  It shows only the parts related to this action and displays in the table the found client (if found).
     *  Shows alerts for invalid input or if client not found.
     */
    @FXML
    private void onSaveSelectClientClick() {
        findById.setVisible(true);
        insert.setVisible(false);
        update.setVisible(false);
        deleteOp.setVisible(false);
        try{
            int idClient = Integer.parseInt(id1.getText());
            Client client = clientBLL.findClientById(idClient);
            List<Client> clientList = new ArrayList<>();
            clientList.add(client);
            TableViewUtility.putInTable(clientTable, clientList);
        }catch(NumberFormatException e){
            showAlert("Invalid input!");
        }catch(NoSuchElementException e){
            showAlert("No client found!");
        }

        id1.clear();
    }

    /**
     *  This method shows only the parts related to the insertion of a client.
     */
    @FXML
    private void onInsertClientClick() {
        findById.setVisible(false);
        insert.setVisible(true);
        update.setVisible(false);
        deleteOp.setVisible(false);
    }

    /**
     *  This method will handle the action to insert a client.
     *  It shows only the parts related to this action and saves the client.
     *  Shows alerts for invalid input or if it failed.
     */
    @FXML
    private void onSaveInsertClientClick() {
        findById.setVisible(false);
        insert.setVisible(true);
        update.setVisible(false);
        deleteOp.setVisible(false);

        try{
            String nameClient = name1.getText();
            String addressClient = address1.getText();
            String emailClient = email1.getText();
            int ageClient = Integer.parseInt(age1.getText());
            String phoneClient = phone1.getText();
            Client client = new Client(nameClient, addressClient, emailClient, ageClient, phoneClient);
            clientBLL.insertClient(client);
        }catch(NumberFormatException e){
            showAlert("Invalid input!");
        }catch(IllegalArgumentException e){
            showAlert("Invalid input!");
        }catch(IllegalStateException e) {
            showAlert("Couldn't insert client!");
        }

        name1.clear();
        address1.clear();
        email1.clear();
        age1.clear();
        phone1.clear();
    }

    /**
     *  This method shows only the parts related to updating a client.
     */
    @FXML
    private void onUpdateClientClick() {
        findById.setVisible(false);
        insert.setVisible(false);
        update.setVisible(true);
        deleteOp.setVisible(false);
    }

    /**
     *  This method will handle the action to update a client.
     *  It shows only the parts related to this action and saves the updates.
     *  Fields left empty won't be updated.
     *  Shows alerts for invalid input or client not found.
     */
    @FXML
    private void onSaveUpdateClientClick() {
        findById.setVisible(false);
        insert.setVisible(false);
        update.setVisible(true);
        deleteOp.setVisible(false);
        try{
            int idClient = Integer.parseInt(id2.getText());
            String nameClient = name2.getText();
            String addressClient = address2.getText();
            String emailClient = email2.getText();
            String ageClient = age2.getText();
            String phoneClient = phone2.getText();

            Client client = clientBLL.findClientById(idClient);

            if(!nameClient.isEmpty()){
                client.setName(nameClient);
            }
            if(!addressClient.isEmpty()){
                client.setAddress(addressClient);
            }
            if(!emailClient.isEmpty()){
                client.setEmail(emailClient);
            }
            if(!ageClient.isEmpty()) {
                client.setAge(Integer.parseInt(ageClient));
            }
            if(!phoneClient.isEmpty()){
                client.setPhone(phoneClient);
            }
            clientBLL.updateClient(client);
        } catch (IllegalArgumentException e){
            showAlert("Invalid input!");
        }catch (NoSuchElementException e){
            showAlert("No client found!");
        }

        id2.clear();
        name2.clear();
        address2.clear();
        email2.clear();
        age2.clear();
        phone2.clear();
    }

    /**
     *  This method shows only the parts related to deleting a client.
     */
    @FXML
    private void onDeleteClientClick() {
        findById.setVisible(false);
        insert.setVisible(false);
        update.setVisible(false);
        deleteOp.setVisible(true);
    }

    /**
     *  This method will handle the action to delete a client.
     *  Deletes a client based on ID.
     *  Shows alerts for invalid input or client not found.
     */
    @FXML
    private void onSaveDeleteClientClick() {
        findById.setVisible(false);
        insert.setVisible(false);
        update.setVisible(false);
        deleteOp.setVisible(true);

        try{
            int idClient = Integer.parseInt(id3.getText());
            Client client = clientBLL.findClientById(idClient);
            clientBLL.deleteClient(client);
        }catch (NoSuchElementException e)
        {
            showAlert("No client found!");
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
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mess);
        alert.showAndWait();
    }

}
