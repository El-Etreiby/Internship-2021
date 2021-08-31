package com.employees.models;


import com.employees.errorHandling.BusinessException;
import lombok.Data;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.time.Month;
import java.time.Year;

@Data
@Entity
public class Salary {
    @EmbeddedId
    private SalaryId id;

    @OneToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;

    @Column(nullable = false)
    private Double grossSalary;

    @Column(nullable = false)
    private Double netSalary;

    private Double bonus;
    private Double raise;
    private Double leaveDeductions;
    private Double taxes;

    public Double applyBonus(Double bonus) throws BusinessException {
        if (bonus > 0.0) {
            this.bonus = bonus;
            this.calculateNetSalary();
            return this.getNetSalary() + bonus;
        } else {
            throw new BusinessException("a bonus must be of a positive value!");
        }

    }

    public void setGrossSalary(Double grossSalary) throws BusinessException {
        Double minimumWage = 588.235294118;
        if (grossSalary < minimumWage) {
            throw new BusinessException("the minimum employee wage is $589!");
        }
        this.grossSalary = grossSalary;
        this.calculateNetSalary();
    }

    private void calculateNetSalary() throws BusinessException {
        Double taxRate = 0.85;
        Double deductions = 500.0;
        Double oneDayDeduction = grossSalary / 30.0;
        if (this.employee != null && this.employee.getDaysOffTaken()!=null && this.employee.getYearsOfExperience()!=null) {
            Integer daysOff = this.employee.getDaysOffTaken();
            if ((this.employee.getYearsOfExperience() < 10 && daysOff > 21)) {
                this.leaveDeductions = oneDayDeduction * (daysOff - 21);
                this.taxes = this.grossSalary * 0.15;
                this.netSalary = this.grossSalary * taxRate - deductions - oneDayDeduction * (daysOff - 21);
                return;
            }
            if (daysOff > 30) {
                this.leaveDeductions = oneDayDeduction * (daysOff - 21);
                this.taxes = this.grossSalary * 0.15;
                this.netSalary = this.grossSalary * taxRate - deductions - oneDayDeduction * (daysOff - 30);
                return;
            }
        }
            this.leaveDeductions = 0.0;
            this.taxes = this.grossSalary * 0.15;
            this.netSalary = this.grossSalary * taxRate - deductions;
            if (this.netSalary < 0.0)
                this.netSalary = 0.0;
    }

    public void raiseEmployee(Double raise) throws BusinessException {
        this.raise = raise;
        this.setGrossSalary(this.getGrossSalary() + raise);
        this.calculateNetSalary();
    }

    @SneakyThrows
    public String toString() {
        this.calculateNetSalary();
        String result = "";
        if (this.employee != null) {
            result += "Employee ID: " + this.employee.getEmployeeId() + "\n";
        }
        result += "Gross salary: " + this.grossSalary + "\n"
                + "Net salary:  " + this.netSalary + "\n"
                + "Bonus: " + this.bonus + "\n"
                + "Raise: " + this.raise + "\n"
                + "taxes: " + this.taxes + "\n"
                + "Leave deductions: " + this.leaveDeductions;
        return result;

    }
}
