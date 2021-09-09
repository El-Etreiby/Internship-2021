package com.employees.models;

import com.employees.errorHandling.BadArgumentException;
import com.employees.errorHandling.InternalException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    @Column(unique = true, nullable = false)
    private Long nationalId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(columnDefinition = "int default 0")
    private Integer yearsOfExperience;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Degree degree;


    private Double bonus;
    private Double raise;   //represents last month's raise (i.e. restarted at the end of each month)

    @Column(nullable = false)
    private Date dob;

    @Column(nullable = false)
    private String gender;

    private Date graduationDate;

    @Column(nullable = false)
    private Double grossSalary;

    private String expertise;

    @Column(columnDefinition = "int default 0")
    private Integer daysOffTaken;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team employeeTeam; //the team the employee belongs to

    @OneToMany(mappedBy = "manager",
            fetch = FetchType.EAGER)
    private List<Employee> managedEmployees;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Salary> salary;

    @OneToOne(mappedBy = "employee")
    @JsonIgnore
    private AccountInformation accountInformation;


    public void setGrossSalary(Double grossSalary) throws BadArgumentException {
            this.grossSalary = grossSalary;
    }

    public void setRaise(Double raise) throws BadArgumentException {
        if (raise == null) {
            return;
        }
        if (raise >= 0.0) {
            this.raise = raise;
        }
    }

    public void setBonus(Double bonus) throws InternalException {
        if (bonus == null) {
            return;
        }
        if (bonus >= 0.0) {
            this.bonus = bonus;
        } else {
            throw new InternalException("a bonus must be of a positive value!");
        }
    }

    public void takeDayOff() {
        if (this.daysOffTaken == null) {
            this.daysOffTaken = 0;
        }

        this.daysOffTaken++;
    }

    public Employee() {
        this.daysOffTaken = 0;
        this.yearsOfExperience = 0;
    }

    public String toString() {
        String result = "ID: " + this.employeeId + "\n"
                + "National ID: " + this.nationalId + "\n"
                + "First name: " + this.firstName + "\n"
                + "last name: " + this.lastName + "\n"
                + "Degree: " + this.degree + "\n"
                + "Years of experience: " + this.yearsOfExperience + "\n"
                + "Days Off taken this year: " + this.daysOffTaken + "\n"
                + "Bonus for this month: " + this.bonus + "\n"
                + "DoB: " + this.dob + "\n"
                + "DoG: " + this.graduationDate + "\n"
                + "Gender: " + this.gender + "\n"
                + "Gross salary: " + this.grossSalary + "\n";
        if (this.department != null) {
            result += "Deaprtment ID: " + this.department.getDepartmentId() + "\n";
        }
        if (this.manager != null) {
            result += "Manager ID: " + this.manager.getEmployeeId() + "\n";
        }
        if (this.employeeTeam != null) {
            result += "Team ID: " + this.employeeTeam.getTeamId() + "\n";
        }
        return result;
    }
}
