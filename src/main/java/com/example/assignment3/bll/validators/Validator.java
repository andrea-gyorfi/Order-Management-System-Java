package com.example.assignment3.bll.validators;

/**
 * This is a generic interface which validates objects
 * @param <T> is the type of the objects it validates.
 */
public interface Validator<T> {

    /**
     * This method validates an object.
     * @param t is the object it validates.
     */
    public void validate(T t);
}
