package com.employees.services;

import com.employees.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employees.repositories.EmployeeRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public void removeEmployee(Integer employeeToBeRemoved) throws Exception {
        Optional<Employee> toBeRemoved = employeeRepository.findById(employeeToBeRemoved);
        if (toBeRemoved.isPresent())
            employeeRepository.deleteById(employeeToBeRemoved);
        else
            throw new Exception("You're trying to delete a non existing employee");


    }

    public static <T> Iterable<T>
    getIterableFromIterator(Iterator<T> iterator) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }
}
