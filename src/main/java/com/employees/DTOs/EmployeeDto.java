package com.employees.DTOs;

import com.employees.models.Degree;
import com.employees.models.Employee;

import java.util.Date;

public class EmployeeDto {
    private Integer employeeId;
    private Date dob;
    private Date graduationDate;
    private String firstName;
    private String lastName;
    private Degree degree;
    private Long nationalId;
    private Integer daysOff;
    private Integer yearsOfExperience;
    private Double grossSalary;
    private Integer managerId;
    private Integer teamId;
    private Integer departmentId;
    private String expertise;
    private char gender;


    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
    }

    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
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

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Long getNationalId() {
        return nationalId;
    }

    public void setNationalId(Long nationalId) {
        this.nationalId = nationalId;
    }

    public Integer getDaysOff() {
        return daysOff;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
        if(yearsOfExperience >= 10) {
            this.daysOff = 30;
        }else{
            this.daysOff=21;
        }
    }

    public EmployeeDto(Employee employee) {
        this.dob = employee.getDob();
        this.employeeId = employee.getEmployeeId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.yearsOfExperience = employee.getYearsOfExperience();
        this.daysOff = employee.getDaysOffTaken();
        this.nationalId = employee.getNationalId();
        this.degree = employee.getDegree();
        this.graduationDate = employee.getGraduationDate();
        this.grossSalary = employee.getGrossSalary();
        this.gender = employee.getGender();
        this.expertise = employee.getExpertise();
        if (employee.getManager() != null)
            this.managerId = employee.getManager().getEmployeeId();
        if (employee.getEmployeeTeam() != null)
            this.teamId = employee.getEmployeeTeam().getTeamId();
        if (employee.getDepartment() != null)
            this.departmentId = employee.getDepartment().getDepartmentId();

    }

    public String toString() {
        String result = "ID: " + this.employeeId + "\n"
                + "National ID: " + this.nationalId + "\n"
                + "First Name: " + this.firstName + "\n"
                + "last Name: " + this.lastName + "\n"
                + "Degree: " + this.degree + "\n"
                + "Years of experience: " + this.yearsOfExperience + "\n"
                + "Days Off taken this year: " + this.daysOff + "\n"
                + "DoB: " + this.dob + "\n"
                + "National ID: " + this.nationalId + "\n"
                + "DoG: " + this.graduationDate + "\n"
                + "Gender: " + this.gender + "\n"
                + "Gross salary: " + this.grossSalary + "\n"
                + "Expertise: " + this.expertise + "\n";
        if (this.departmentId != null)
            result += "Department ID: " + this.departmentId + "\n";
        if (this.managerId != null)
            result += "Manager ID: " + this.managerId + "\n";
        if (this.teamId != null)
            result += "Team ID: " + this.teamId;
        return result;
    }
}
