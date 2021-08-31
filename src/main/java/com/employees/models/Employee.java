package com.employees.models;

import com.employees.errorHandling.BusinessException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@EqualsAndHashCode
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    @Column(unique = true)
    private Long nationalId;

    private String firstName;
    private String lastName;
    private Integer yearsOfExperience;
    @Enumerated(EnumType.STRING)
    private Degree degree;
    private Date dob;
    private char gender;
    private Date graduationDate;
    private Double grossSalary;
    private String expertise;
    private Integer daysOffTaken;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonBackReference
    private Department department;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team employeeTeam; //the team the employee belongs to

    @OneToMany(mappedBy = "manager",
            cascade = CascadeType.ALL )
    private List<Employee> managedEmployees;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToOne(mappedBy = "employee")
    private Salary salary;

    @OneToOne(mappedBy = "employee")
    private AccountInformation accountInformation;


    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) throws BusinessException {
        Double minimumWage = 588.235294118;
        if(grossSalary < minimumWage){
            throw new BusinessException("the minimum employee wage is $589!");
        }
        this.grossSalary = grossSalary;
        //System.out.println("Net salary updated: " + this.netSalary + " -------------------");

    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getNationalId() {
        return nationalId;
    }

    public void setNationalId(Long nationalId) {
        this.nationalId = nationalId;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public Integer getDaysOffTaken() {
        return daysOffTaken;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public void raiseEmployeeSalary(Double raise) throws BusinessException {
        if (raise > 0.0) {
            this.setGrossSalary(this.getGrossSalary() + raise);
            return;
        }
        throw new BusinessException("a raise must be of a positive value!");
    }

    public void takeDayOff(){
        if(this.daysOffTaken==null){
            this.daysOffTaken=0;
        }

        this.daysOffTaken++;
    }

    public Employee() {
        this.daysOffTaken=0;
    }

    public String toString() {
        String result = "";
        return "ID: " + this.employeeId + "\n"
                + "National ID: " + this.nationalId + "\n"
                + "First name: " + this.firstName + "\n"
                + "last name: " + this.lastName + "\n"
                + "Degree: " + this.degree + "\n"
                + "Years of experience: " + this.yearsOfExperience + "\n"
                + "Days Off taken this year: " + this.daysOffTaken + "\n"
                + "DoB: " + this.dob + "\n"
                + "DoG: " + this.graduationDate + "\n"
                + "Gender: " + this.gender + "\n"
                + "Gross salary: " + this.grossSalary + "\n";
    }
}
