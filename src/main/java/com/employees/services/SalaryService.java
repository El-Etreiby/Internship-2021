package com.employees.services;

import com.employees.errorHandling.EmployeeNotFoundException;
import com.employees.errorHandling.SalaryNotFoundException;
import com.employees.models.Employee;
import com.employees.models.Salary;
import com.employees.models.SalaryId;
import com.employees.repositories.EmployeeRepository;
import com.employees.repositories.SalaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.employees.services.IssueSalaries.issueSalary;

@Service
@AllArgsConstructor
public class SalaryService {
    private SalaryRepository salaryRepository;
    private EmployeeRepository employeeRepository;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public Salary getSomeSalaryHistory(SalaryId salaryId) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(salaryId.getEmployee_id());
        if(!employee.isPresent()){
            throw new EmployeeNotFoundException("this employee does not exist");
        }
        Optional<Salary> salary = salaryRepository.findById(salaryId);
        if(!salary.isPresent()){
            throw new SalaryNotFoundException("this employee was not paid in the provided date");
        }
        return salary.get();
    }

    public List<Salary> getAllSalaryHistory(Integer intEmployeeId) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(intEmployeeId);
        if(!employee.isPresent()){
            throw new EmployeeNotFoundException("this employee does not exist");
        }
        return salaryRepository.getByEmployeeId(intEmployeeId);
    }

    @Scheduled(cron = "0 00 12 L * ?", zone = "Africa/Cairo")
    public void issueSalaries() throws ParseException {
        Iterable<Employee> allEmployees = employeeRepository.findAll();
        Iterator<Employee> employees = allEmployees.iterator();

        String dateAsString = dateFormat.format(new Date());
        Date date = null;
        date = dateFormat.parse(dateAsString);
        int month = date.getMonth();
        int year = date.getYear();

        Employee temp;
        while(employees.hasNext()) {
            temp = employees.next();
            Salary newSalary = issueSalary(temp);
            salaryRepository.save(newSalary);
            temp.setBonus(0.0);
            if (temp.getRaise() == null) {
                temp.setRaise(0.0);
            }
            // System.out.println("g: " + temp.getGrossSalary() + ". r: " + temp.getRaise());
            temp.setGrossSalary(temp.getGrossSalary() + temp.getRaise());
            temp.setRaise(0.0);
            if (temp.getYearsOfExperience() < 10 && temp.getDaysOffTaken() > 21) {
                temp.setDaysOffTaken(21);
            }
            if (temp.getYearsOfExperience() > 10 && temp.getDaysOffTaken() > 30) {
                temp.setDaysOffTaken(30);
            }
            if (month == 12) {
                temp.setDaysOffTaken(0);
                temp.setYearsOfExperience(temp.getYearsOfExperience() + 1);
            }
//                employeeRepository.save(temp);
            System.out.println("employee after issue: " + "\n" + temp);
            employeeRepository.save(temp);
        }

    }
}
