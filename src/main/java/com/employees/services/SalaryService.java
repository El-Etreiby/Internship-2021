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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SalaryService {
    private SalaryRepository salaryRepository;
    private EmployeeRepository employeeRepository;

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
}
