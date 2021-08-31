package com.employees.models;


import com.sun.istack.internal.NotNull;

import javax.persistence.*;

@Entity
public class AccountInformation {

    @Id
    private String username;

    private String password;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
