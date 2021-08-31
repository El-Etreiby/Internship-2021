package com.employees.errorHandling;

public class SalaryNotFoundException extends Exception{
    public SalaryNotFoundException(){
        super();
    }
    public SalaryNotFoundException(String message){
        super(message);
    }
}
