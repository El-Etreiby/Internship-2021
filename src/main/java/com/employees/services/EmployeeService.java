package com.employees.services;

import com.employees.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employees.repositories.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;
    public void saveEmployee(Employee employee){
        employeeRepository.save(employee);
    }
}
