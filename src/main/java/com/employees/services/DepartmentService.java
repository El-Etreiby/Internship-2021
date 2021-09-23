package com.employees.services;


import com.employees.errorHandling.BadArgumentException;
import com.employees.errorHandling.DepartmentNotFoundException;
import com.employees.errorHandling.InternalException;
import com.employees.models.Department;
import com.employees.models.Employee;
import com.employees.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employees.repositories.DepartmentRepository;

import java.util.Iterator;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentService {
    private DepartmentRepository departmentRepository;
    private EmployeeRepository employeeRepository;

    public void addNewDepartment(Department department){
        Iterable<Department> departments = departmentRepository.findAll();
        Iterator<Department> allDepartments = departments.iterator();
        while(allDepartments.hasNext()){
            if(allDepartments.next().getDepartmentName().equals(department.getDepartmentName())){
                throw new InternalException("Cannot create teams with duplicate names");
            }
        }
        departmentRepository.save(department);
    }

    public void removeDepartment(Integer departmentToBeRemoved) {
        Optional<Department> toBeRemoved = departmentRepository.findById(departmentToBeRemoved);
        if (toBeRemoved.isPresent()) {
            Iterable<Employee> employees = employeeRepository.findAll();
            Iterator<Employee> allEmployees = employees.iterator();
            while(allEmployees.hasNext()){
                Employee temp = allEmployees.next();
                System.out.println("next employee: " + temp);
                System.out.println(temp.getDepartment());
                if(temp.getDepartment()!=null && temp.getDepartment().getDepartmentId().equals(departmentToBeRemoved)){
                    throw new BadArgumentException("this department has members in it and cannot be deleted");
                }
            }
            departmentRepository.deleteByID(departmentToBeRemoved);
        }
        else
            throw new DepartmentNotFoundException("You're trying to delete a non existing department");
    }

    public void updateDepartment(String newName, int departmentId) {
        Optional<Department> toBeUpdated = departmentRepository.findById(departmentId);
        if (!toBeUpdated.isPresent()) {
            throw new DepartmentNotFoundException("you're trying to update a non existing department");
        }
        Department department = toBeUpdated.get();
        department.setDepartmentName(newName);
        departmentRepository.save(department);
    }
}
