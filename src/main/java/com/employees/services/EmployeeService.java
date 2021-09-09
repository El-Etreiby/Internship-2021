package com.employees.services;

import com.employees.errorHandling.*;
import com.employees.models.AccountInformation;
import com.employees.models.Employee;
import com.employees.models.Salary;
import com.employees.models.SalaryId;
import com.employees.repositories.AccountInformationRepository;
import com.employees.repositories.EmployeeRepository;
import com.employees.repositories.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private AccountInformationRepository accountInformationRepository;

    public String requestVacation(Integer employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
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

    public String changeUsername(String newUsername) throws BadArgumentException {
        Optional<AccountInformation> duplicate = accountInformationRepository.findByUsername(newUsername);
        if(duplicate.isPresent()){
            throw new BadArgumentException("this username already exists");
        }
        String username = this.getUsername();
        Optional<AccountInformation> account = accountInformationRepository.findByUsername(username);
 //       System.out.println("OLD: " + username + account.isPresent());
        if (account.isPresent()) {
            AccountInformation updatedAccount = account.get();
            accountInformationRepository.deleteByUsername(username);
            Optional<AccountInformation> oldAccount = accountInformationRepository.findByUsername(username);
            System.out.println("oldA : " + oldAccount.isPresent());
            updatedAccount.setUsername(newUsername);
            accountInformationRepository.save(updatedAccount);
            return "username updated successfully!";
        }
        return "failed to update username";
    }

    public String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication.getName();
    }

//    public Salary getSomeSalaryHistory(SalaryId salaryId) throws EmployeeNotFoundException, BadArgumentException, InternalException, SalaryNotFoundException {
//        Optional<AccountInformation> account = accountInformationRepository.findByUsername(this.getUsername());
//        if (!account.isPresent()) {
//            throw new BadArgumentException("account does not exist");
//        }
//        Optional<Employee> employee = employeeRepository.findById(salaryId.getEmployee_id());
//        if (!employee.isPresent()) {
//            throw new EmployeeNotFoundException("this employee does not exist");
//        }
//        if (account.get().getEmployee().getEmployeeId() != employee.get().getEmployeeId()) {
//            throw new InternalException("you're only allowed to view your own salary");
//        }
//        Optional<Salary> salary = salaryRepository.findById(salaryId);
//        if (!salary.isPresent()) {
//            throw new SalaryNotFoundException("this employee was not paid in the provided date");
//        }
//        return salary.get();
//    }

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
            updatedAccount.setPassword(newPassword);
            accountInformationRepository.save(updatedAccount);
            return "password updated successfully!";
        }
        return "failed to update password";
    }

//    public Salary getSomeSalaryHistory(String month, String year) {
//        String username = this.getUsername();
//        Optional<AccountInformation> account = accountInformationRepository.findByUsername(username);
//        //       System.out.println("OLD: " + username + account.isPresent());
//        if (account.isPresent()) {
//            AccountInformation acc = account.get();
//            SalaryId salaryId = new SalaryId();
//            salaryId.setYear(Integer.parseInt(year));
//            salaryId.setMonth(Integer.parseInt(month));
//            salaryId.setEmployee_id(acc.getEmployee().getEmployeeId());
//            Optional<Salary> salary = salaryRepository.findById(salaryId);
//            if(!salary.isPresent()){
//                throw new SalaryNotFoundException("this employee was not paid in the provided date");
//            }
//            return salary.get();
//        }
//        throw new SalaryNotFoundException("this employee was not paid in the provided date");
//    }
}
