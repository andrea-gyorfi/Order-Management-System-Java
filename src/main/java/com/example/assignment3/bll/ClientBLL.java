package com.example.assignment3.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.example.assignment3.bll.validators.ClientNameValidator;
import com.example.assignment3.bll.validators.ClientPhoneValidator;
import com.example.assignment3.bll.validators.ClientEmailValidator;
import com.example.assignment3.bll.validators.Validator;
import com.example.assignment3.dao.ClientDAO;
import com.example.assignment3.model.Client;

/**
 * This class performs the CRUD operations related to clients.
 */
public class ClientBLL {

    private List<Validator<Client>> validators;

    public ClientBLL() {
        validators = new ArrayList<Validator<Client>>();
        validators.add(new ClientEmailValidator());
        validators.add(new ClientPhoneValidator());
        validators.add(new ClientNameValidator());
    }

    /**
     * This method finds a client based on the id.
     * @param id is the client's id it searches for.
     * @return the client that has the given from the database.
     */
    public Client findClientById(int id) {
        Client st = new ClientDAO().findById(id);
        if (st == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return st;
    }

    /**
     * This method returns a list of all the clients from the database.
     * @return the list of the clients.
     */
    public List<Client> findAll() {
        List<Client> clients = new ClientDAO().findAll();
        if (clients.isEmpty()) {
            throw new NoSuchElementException("No clients found!");
        }
        return clients;
    }

    /**
     * This method inserts a client in the database.
     * @param client is the client who will be inserted.
     */
    public void insertClient(Client client) {
        validators.forEach(v -> v.validate(client));
        Client cl = new ClientDAO().insert(client);
        if (cl == null) {
            throw new IllegalStateException("The client with id = " + client.getId() + "could not be inserted!");
        }
    }

    /**
     * This method updates a client from the database.
     * @param client is the client to be updated.
     */
    public void updateClient(Client client) {
        validators.forEach(v -> v.validate(client));
        Client cl = new ClientDAO().update(client);
        if (cl == null) {
            throw new NoSuchElementException("The client with id =" + client.getId() + " could not be updated!");
        }
    }

    /**
     * This method deletes a client from the database.
     * @param client is the client to be deleted.
     */
    public void deleteClient(Client client) {
        Client cl = new ClientDAO().delete(client);
        if (cl == null) {
            throw new NoSuchElementException("The client with id =" + client.getId() + " could not be deleted!");
        }
    }
}
