package com.employees.services;

import com.employees.errorHandling.BusinessException;
import com.employees.errorHandling.EmployeeNotFoundException;
import com.employees.errorHandling.SalaryNotFoundException;
import com.employees.models.Employee;
import com.employees.models.Salary;
import com.employees.models.SalaryId;
import com.employees.repositories.EmployeeRepository;
import com.employees.repositories.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

//    public void addBonusToEmployee(Integer employeeId, Double bonus) throws EmployeeNotFoundException, BusinessException {
//        Optional<Employee> employee = employeeRepository.findById(employeeId);
//        if (!employee.isPresent()) {
//            throw new EmployeeNotFoundException("This employee does not exist!");
//        }
//        employee.get().applyBonus(bonus);
//        employeeRepository.save(employee.get());
//
//    }

    public Salary getSomeSalaryHistory(SalaryId salaryId) throws EmployeeNotFoundException, SalaryNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(salaryId.getEmployeeId());
        if(!employee.isPresent()){
            throw new EmployeeNotFoundException("this employee does not exist!");
        }
        Optional<Salary> salary = salaryRepository.findById(salaryId);
        if(!salary.isPresent()){
            throw new SalaryNotFoundException("this employee was not paid in the provided date!");
        }
        return salary.get();
    }

    public List<Salary> getAllSalaryHistory(Integer intEmployeeId) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(intEmployeeId);
        if(!employee.isPresent()){
            throw new EmployeeNotFoundException("this employee does not exist!");
        }
        List<Salary> salaries = salaryRepository.getByEmployeeId(intEmployeeId);
        return salaries;
    }
}
