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
    private char gender;


    public EmployeeDto(Employee employee) {
        this.dob = employee.getDob();
        this.employeeId = employee.getEmployeeId();
        this.employeeName = employee.getEmployeeName();
        this.graduationDate = employee.getGraduationDate();
        this.grossSalary = employee.getGrossSalary();
        this.netSalary = employee.getNetSalary();
        this.gender = employee.getGender();
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
                + "Net salary: " + this.netSalary + "\n";
        if (this.departmentId != null)
            result += "Department ID: " + this.departmentId + "\n";
        if (this.managerId != null)
            result += "Manager ID: " + this.managerId + "\n";
        if (this.teamId != null)
            result += "Team ID: " + this.teamId;
        return result;
    }
}
