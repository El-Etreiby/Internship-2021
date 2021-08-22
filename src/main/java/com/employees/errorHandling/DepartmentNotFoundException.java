package com.employees.errorHandling;

public class DepartmentNotFoundException extends Exception{
    public DepartmentNotFoundException(){
        super();
    }
    public DepartmentNotFoundException(String message){
        super(message);
    }
}
