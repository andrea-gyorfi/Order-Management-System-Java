package com.example.assignment3.bll.validators;

import com.example.assignment3.model.Client;

/**
 *  This class validates the client's name.
 */
public class ClientNameValidator implements Validator<Client>{
    private static final int NAME_LENGTH = 3;

    /**
     * The method check a client's name.
     * @param client is the client for who will check if the name has a length >= 3.
     */
    public void validate(Client client) {
        String name = client.getName();
        if (name == null || name.length() < NAME_LENGTH) {
            throw new IllegalArgumentException("Name is not valid!");
        }
    }
}
