package com.employees.DTOs;

import com.employees.models.Degree;
import com.employees.models.Employee;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
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
    private String gender;


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
