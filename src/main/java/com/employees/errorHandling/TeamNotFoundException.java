package com.employees.errorHandling;

public class TeamNotFoundException extends Exception{
    public TeamNotFoundException(){
        super();
    }
    public TeamNotFoundException(String message){
        super(message);
    }
}
