package com.employees.DTOs;

import com.employees.models.Employee;

import java.util.Date;

public class EmployeeDto {
    private Integer employeeId;
    private Date dob;
    private Date graduationDate;
    private String employeeName;
    private Double grossSalary;
    private Double netSalary;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
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

    public EmployeeDto(Employee employee) {
        this.dob = employee.getDob();
        this.employeeId = employee.getEmployeeId();
        this.employeeName = employee.getEmployeeName();
        this.graduationDate = employee.getGraduationDate();
        this.grossSalary = employee.getGrossSalary();
        this.netSalary = employee.getNetSalary();
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
                + "Name: " + this.employeeName + "\n"
                + "DoB: " + this.dob + "\n"
                + "DoG: " + this.graduationDate + "\n"
                + "Gender: " + this.gender + "\n"
                + "Gross salary: " + this.grossSalary + "\n"
                + "Net salary: " + this.netSalary + "\n"
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
