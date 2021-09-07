package com.employees.models;

import com.employees.errorHandling.BusinessException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
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
    private Integer daysOffTaken;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private Team employeeTeam; //the team the employee belongs to

    @OneToMany(mappedBy = "manager",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Employee> managedEmployees;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "employee",
            cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Salary> salary;

    @OneToOne(mappedBy = "employee",
            cascade = CascadeType.ALL)
    @JsonIgnore
    private AccountInformation accountInformation;


    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) throws BusinessException {
        Double minimumWage = 588.235294118;
        if (grossSalary < minimumWage) {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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

    public void setRaise(Double raise) throws BusinessException {
        if (raise == null) {
            return;
        }
        if (raise >= 0.0) {
            this.raise = raise;
            return;
        }
        throw new BusinessException("a raise must be of a positive value!");
    }

    public void setBonus(Double bonus) throws BusinessException {
        if (bonus == null) {
            return;
        }
        if (bonus >= 0.0) {
            this.bonus = bonus;
        } else {
            throw new BusinessException("a bonus must be of a positive value!");
        }
    }

    public void applyRaise(Double raise) throws BusinessException {
        if (raise > 0.0) {
            this.raise = raise;
        } else {
            throw new BusinessException("a raise must be of a positive value!");
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
