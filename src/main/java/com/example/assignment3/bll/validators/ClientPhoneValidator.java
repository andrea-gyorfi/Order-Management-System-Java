package com.example.assignment3.bll.validators;

import com.example.assignment3.model.Client;

/**
 *  This class validates the client's phone number.
 */
public class ClientPhoneValidator implements Validator<Client> {
    private static final int PHONE_LENGTH = 10;

    /**
     * This method checks a client's phone number.
     * @param client is the client for who will check if the phone number has exactly 10 characters.
     */
    public void validate(Client client) {
        String phone = client.getPhone();
        if (phone == null || phone.length() != PHONE_LENGTH) {
            throw new IllegalArgumentException("Phone number is not valid!");
        }
    }
}
