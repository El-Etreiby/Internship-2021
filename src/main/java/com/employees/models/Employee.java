package com.employees.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer employeeId;

    // private Set<String> expertise;
    private String name;
    private Date dob;
    private char gender;
    private Date graduationDate;
    private Double grossSalary;
    private Double netSalary;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team employeeTeam; //the team the employee belongs to

    @OneToMany(mappedBy = "manager",
    orphanRemoval = true,
    cascade = CascadeType.ALL)
    private List<Employee> managedEmployees;

    @ManyToOne
    @JoinColumn(name = "managerId")
    private Employee manager;

    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

//    public Set<String> getExpertise() {
//        return expertise;
//    }
//
//    public void setExpertise(Set<String> expertise) {
//        this.expertise = expertise;
//    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public List<Employee> getManagedEmployees() {
        return managedEmployees;
    }

    public void setManagedEmployees(List<Employee> managedEmployees) {
        this.managedEmployees = managedEmployees;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }


    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
    }

    public Team getEmployeeTeam() {
        return employeeTeam;
    }

    public void setEmployeeTeam(Team employeeTeam) {
        this.employeeTeam = employeeTeam;
    }


    //    public Employee(Integer employeeId, Set<String> expertise, String department, String name, Date dob, char gender, Date graduationDate, Double grossSalary, Employee manager, ArrayList<Employee> managedEmployees, Team employeeTeam, Team managedTeam) {
//        this.employeeId = employeeId;
//        this.expertise = expertise;
//        this.department = department;
//        this.name = name;
//        this.dob = dob;
//        this.gender = gender;
//        this.graduationDate = graduationDate;
//        this.grossSalary = grossSalary;
//        this.employeeTeam = employeeTeam;
//        this.managedTeam = managedTeam;
//    }
    public Employee() {

    }
}
