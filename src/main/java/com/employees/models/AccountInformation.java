package com.employees.models;


import lombok.Data;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Random;

@Entity
@Data
public class AccountInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    @Column(unique = true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String role;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "employee_id", nullable=false)
    private Employee employee;

    public String getUsername() {
        return username;
    }

    public void setRole(String role){
        this.role = "ROLE_"+role;
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

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer id) {
        this.accountId = id;
    }

    public String getRole() {
        return role;
    }
}
