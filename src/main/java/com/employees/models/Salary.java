package com.employees.models;

import com.employees.errorHandling.InternalException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Salary {
    //gross salary would represent the salary before adding
    // or subtracting any values.
    //net salary represents the salary after all modification.
    @EmbeddedId
    private SalaryId id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;

    @Column(nullable = false)
    private Double grossSalary;

    private Double netSalary;

    private Double bonus;
    private Double raise;
    private Double leaveDeductions;
    private Double taxes;

    public void setGrossSalary(Double grossSalary) throws InternalException {
        Double minimumWage = 588.235294118;
        if (grossSalary < minimumWage) {
            throw new InternalException("the minimum employee wage is $589!");
        }
        this.grossSalary = grossSalary;
    }

    public void calculateNetSalary() throws InternalException {
        Double taxRate = 0.85;
        Double insurance = 500.0;
        Double oneDayDeduction = grossSalary / 30.0;
        if (this.employee != null && this.employee.getDaysOffTaken() != null && this.employee.getYearsOfExperience() != null) {
            Integer daysOff = this.employee.getDaysOffTaken();
            if ((this.employee.getYearsOfExperience() < 10 && daysOff > 21)) {
                this.leaveDeductions = oneDayDeduction * (daysOff - 21);
                this.taxes = (this.grossSalary + this.bonus + this.raise - insurance - this.leaveDeductions) * 0.15;
                this.netSalary = (this.grossSalary + this.bonus + this.raise - insurance - this.leaveDeductions) * taxRate;
                return;
            }
            if (this.employee.getYearsOfExperience() > 10 && daysOff > 30) {
                this.leaveDeductions = oneDayDeduction * (daysOff - 30);
                this.taxes = (this.grossSalary + this.bonus + this.raise - insurance - this.leaveDeductions);
                this.netSalary = (this.grossSalary + this.bonus + this.raise - insurance - this.leaveDeductions) * taxRate;
                return;
            }
        }
        this.leaveDeductions = 0.0;
        this.taxes = (this.grossSalary + this.raise + this.bonus - insurance) * 0.15;
        this.netSalary = (this.grossSalary + this.raise + this.bonus - insurance)* taxRate;
        if (this.netSalary < 0.0)
            this.netSalary = 0.0;
    }

    public void setEmployee(Employee employee) throws InternalException {
        this.employee = employee;
        if (employee.getGrossSalary() != null) {
            this.setGrossSalary(employee.getGrossSalary());
        }
        if(employee.getBonus()!=null){
            this.bonus=employee.getBonus();
        }
        if(employee.getRaise()!=null){
            this.raise=employee.getRaise();
        }
    }

    @SneakyThrows
    public String toString() {
        String result = "";
        if (this.employee != null) {
            result += "Employee ID: " + this.employee.getEmployeeId() + "\n";
        }
        result += "Month: " + this.id.getMonth() + "\n"
                + "Year: " + this.id.getYear() + "\n"
                + "Gross salary: " + this.grossSalary + "\n"
                + "Bonus: " + this.bonus + "\n"
                + "Raise: " + this.raise + "\n"
                + "taxes: " + this.taxes + "\n"
                + "Leave deductions: " + this.leaveDeductions;
        if(this.netSalary!=null){
            result += "Net salary:  " + this.netSalary + "\n";
        }

        return result;
    }
}
