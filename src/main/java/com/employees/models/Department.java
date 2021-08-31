package com.employees.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;
    private String departmentName;

    @OneToMany(
            mappedBy = "department",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Employee> departmentMembers;


    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String name) {
        this.departmentName = name;
    }

    public List<Employee> getDepartmentMembers() {
        return departmentMembers;
    }

    public void setDepartmentMembers(List<Employee> departmentMembers) {
        this.departmentMembers = departmentMembers;
    }
}
