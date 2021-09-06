package com.employees.errorHandling;

public class MissingInputException extends Exception{
    public MissingInputException(){
        super();
    }
    public MissingInputException(String message){
        super(message);
    }
}

