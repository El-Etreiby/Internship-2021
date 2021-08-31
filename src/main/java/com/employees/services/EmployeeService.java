package com.employees.services;

import com.employees.models.Employee;
import com.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public int requestVacation(Integer employeeId){
      Optional<Employee> employee = employeeRepository.findById(employeeId);
      Employee toBeUpdated = employee.get();
      toBeUpdated.takeDayOff();
      employeeRepository.save(toBeUpdated);
      return toBeUpdated.getDaysOffTaken();
    }

}
