package com.employees.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter

public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;

    @Column(unique = true, nullable = false)
    private String departmentName;

    public String toString(){
        return "department ID: " + this.departmentId + "\n"
                + "department name: " + this.departmentName + "\n";
    }
}
