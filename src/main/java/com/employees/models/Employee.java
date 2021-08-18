package com.employees.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@SqlResultSetMapping(name = "Employee")
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer employeeId;

    // private Set<String> expertise;
    private String employeeName;
    private Date dob;
    private char gender;
    private Date graduationDate;
    private Double grossSalary;
    private Double netSalary;
    private String expertise;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team employeeTeam; //the team the employee belongs to

    @OneToMany(mappedBy = "manager",
    cascade = CascadeType.ALL)
   // @JsonIgnore
    private List<Employee> managedEmployees;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) {
        this.grossSalary = grossSalary;
        this.setNetSalary();
        //System.out.println("Net salary updated: " + this.netSalary + " -------------------");

    }

    public Integer getEmployeeId() {
        return employeeId;
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
        this.setNetSalary();
        return netSalary;
    }

    public void setNetSalary(){
        if(this.grossSalary!=null)
        this.netSalary = this.grossSalary*0.85-500;

       // System.out.println("Net salary updated1: " + this.netSalary + " -------------------");
    }

//    public void setNetSalary(Double netSalary) {
//        if(this.grossSalary!=null)
//            this.netSalary = this.grossSalary*0.85-500;
//
//        System.out.println("Net salary updated: " + this.netSalary + " -------------------");
//    }

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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String name) {
        this.employeeName = name;
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

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
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
    public String toString(){
        return "ID: " + this.employeeId + "\n"
                + "Team ID:  " + this.employeeTeam.getTeamId() + "\n"
             + "Name: " + this.employeeName + "\n"
             + "DoB: " + this.dob + "\n"
             + "DoG: " + this.graduationDate + "\n"
             + "Gender: " + this.gender + "\n"
             + "Gross salary: " + this.grossSalary + "\n"
             + "Net salary: "  + this.netSalary + "\n"
                + "Department ID: " + this.getDepartment().getDepartmentId() + "\n"
                + "Manager ID: " + this.getManager().getEmployeeId();
    }
}
