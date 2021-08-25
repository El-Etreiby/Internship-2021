package com.employees.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer departmentId;
    private String departmentName;
//many side is the owning side (owns FK)!!!!
    // joinColumn("fk name")

    @OneToMany(
            mappedBy="department",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
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
