package com.employees.errorHandling;

public class InvalidUsernameException extends Exception {
    public InvalidUsernameException(){
        super();
    }
    public InvalidUsernameException(String message){
        super(message);
    }
}
