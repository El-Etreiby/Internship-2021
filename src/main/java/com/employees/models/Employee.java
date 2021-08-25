package com.employees.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@SqlResultSetMapping(name = "Employee")
@Entity
public class Employee {

//    @SequenceGenerator(name= "EMPLOYEE_SEQUENCE", sequenceName = "EMPLOYEE_SEQUENCE_ID", initialValue=1, allocationSize = 1)
//    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="EMPLOYEE_SEQUENCE")

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

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Double getNetSalary() {
        this.setNetSalary();
        return netSalary;
    }

    public void setNetSalary(){
        Double taxRate = 0.85;
        Double deductions = 500.0;
        if(this.grossSalary!=null)
        this.netSalary = this.grossSalary*taxRate-deductions;
            if(this.netSalary < 0.0)
                this.netSalary = 0.0;
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
