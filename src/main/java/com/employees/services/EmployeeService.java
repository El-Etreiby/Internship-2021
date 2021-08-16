package com.employees.services;

import com.employees.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employees.repositories.EmployeeRepository;

import java.util.Iterator;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public String addNewEmployee(Employee employee) {
        employeeRepository.save(employee);
        return "Employee added successfully!";
    }

    public String removeEmployee(Integer employeeToBeRemoved) throws Exception {
        Optional<Employee> toBeRemoved = employeeRepository.findById(employeeToBeRemoved);
        if (toBeRemoved.isPresent()) {
            employeeRepository.deleteById(employeeToBeRemoved);
            return "Employee deleted successfully!";
        }
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

    public void addEmployeeToTeam(Integer employeeId, Integer teamId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent()) {
            throw new Exception("This employee does not exist");
        }
        employeeRepository.addEmployeeToTeam(employeeId,teamId);
    }

//        } //modification beygeely employee object
        //REST naming convention for update --> path variables
        //search for query DSL

}
