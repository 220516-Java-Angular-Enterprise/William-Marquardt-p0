package com.revature.storeApp.util.custom_exceptions;


//Created a custom exception in order to throw exceptions when users put in an invalid input
public class InvalidUserInputException extends RuntimeException {

    public InvalidUserInputException(String message) {
        super(message);
    }

}
