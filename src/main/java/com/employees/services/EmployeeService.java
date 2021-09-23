package com.employees.services;

import com.employees.errorHandling.*;
import com.employees.models.AccountInformation;
import com.employees.models.Employee;
import com.employees.models.Salary;
import com.employees.models.SalaryId;
import com.employees.repositories.AccountInformationRepository;
import com.employees.repositories.EmployeeRepository;
import com.employees.repositories.SalaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private SalaryRepository salaryRepository;
    private AccountInformationRepository accountInformationRepository;

    public String requestVacation() {
        Optional<AccountInformation> account = accountInformationRepository.findByUsername(this.getUsername());
        Optional<Employee> employee = employeeRepository.findById(account.get().getEmployee().getEmployeeId());
        Employee toBeUpdated = employee.get();
        toBeUpdated.takeDayOff();
        employeeRepository.save(toBeUpdated);
        int daysOff = toBeUpdated.getDaysOffTaken();
        if(toBeUpdated.getYearsOfExperience() < 10 && daysOff <= 21){
            return "Request submitted successfully. You have " + (21-daysOff) + " days off left this year";
        }
        if(toBeUpdated.getYearsOfExperience() < 10 && daysOff > 21){
            return "Request submitted successfully. You exceeded this month's days off by " + (daysOff-21) + " days.";
        }
        if(toBeUpdated.getYearsOfExperience() >= 10 && daysOff <= 30){
            return "Request submitted successfully. You exceeded this month's days off by " + (30-daysOff) + " days.";
        }
            return "Request submitted successfully. You exceeded this month's days off by " + (daysOff-30) + " days.";

    }

    public String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication.getName();
    }


    public List<Salary> getAllSalaryHistory() throws EmployeeNotFoundException, BadArgumentException, InternalException {
        Optional<AccountInformation> account = accountInformationRepository.findByUsername(this.getUsername());
        if (!account.isPresent()) {
            throw new BadArgumentException("account does not exist!");
        }
        Optional<Employee> employee = employeeRepository.findById(account.get().getEmployee().getEmployeeId());
        if (!employee.isPresent()) {
            throw new EmployeeNotFoundException("this employee does not exist!");
        }
        return employee.get().getSalary();
    }

    public int getDaysOffByDate(String month, String year) throws BadArgumentException, EmployeeNotFoundException, SalaryNotFoundException {
        Optional<AccountInformation> account = accountInformationRepository.findByUsername(this.getUsername());
        if (!account.isPresent()) {
            throw new BadArgumentException("account does not exist!");
        }
        Optional<Employee> employee = employeeRepository.findById(account.get().getEmployee().getEmployeeId());
        if (!employee.isPresent()) {
            throw new EmployeeNotFoundException("this employee does not exist!");
        }
        SalaryId salaryId = new SalaryId();
        salaryId.setYear(Integer.parseInt(year));
        salaryId.setMonth(Integer.parseInt(month));
        salaryId.setEmployee_id(account.get().getEmployee().getEmployeeId());
        Optional<Salary> salary = salaryRepository.findById(salaryId);
        if (!salary.isPresent()) {
            throw new SalaryNotFoundException("this employee was not paid in the provided date");
        }
        Double leaveDeductions = salary.get().getLeaveDeductions();
        return (int) (leaveDeductions * 30.0 / salary.get().getGrossSalary()) +21;
    }

    public String changePassword(String newPassword) {
        String username = this.getUsername();
        Optional<AccountInformation> account = accountInformationRepository.findByUsername(username);
        //       System.out.println("OLD: " + username + account.isPresent());
        if (account.isPresent()) {
            AccountInformation updatedAccount = account.get();
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String encryptedPassword = encoder.encode(newPassword);
            updatedAccount.setPassword(encryptedPassword);
            accountInformationRepository.save(updatedAccount);
            return "password updated successfully!";
        }
        return "failed to update password";
    }

    public int getDaysOff() {
        Optional<AccountInformation> account = accountInformationRepository.findByUsername(this.getUsername());
        Optional<Employee> employee = employeeRepository.findById(account.get().getEmployee().getEmployeeId());
        return employee.get().getDaysOffTaken();
    }
}
