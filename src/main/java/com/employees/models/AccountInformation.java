package com.employees.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class AccountInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    @Column(unique = true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false, columnDefinition = "varchar(255) default 'EMPLOYEE'")
    private String role;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable=false)
    private Employee employee;

    public void setRole(String role){
        this.role = role;
    }
}
