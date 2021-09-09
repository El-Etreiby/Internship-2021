package com.employees.services;


import com.employees.errorHandling.InternalException;
import com.employees.models.Department;
import com.employees.models.Employee;
import com.employees.models.Team;
import com.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employees.repositories.DepartmentRepository;

import java.util.Iterator;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public String addNewDepartment(Department department){
        Iterable<Department> departments = departmentRepository.findAll();
        Iterator<Department> allDepartments = departments.iterator();
        while(allDepartments.hasNext()){
            if(allDepartments.next().getDepartmentName() == department.getDepartmentName()){
                throw new InternalException("Cannot create teams with duplicate names");
            }
        }
        departmentRepository.save(department);
        return "Department added successfully!";
    }

    public String removeDepartment(Integer departmentToBeRemoved) throws Exception {
        Optional<Department> toBeRemoved = departmentRepository.findById(departmentToBeRemoved);
        if (toBeRemoved.isPresent()) {
            Iterable<Employee> employees = employeeRepository.findAll();
            Iterator<Employee> allEmployees = employees.iterator();
            while(allEmployees.hasNext()){
                if(allEmployees.next().getDepartment()!=null && allEmployees.next().getDepartment().getDepartmentId()==departmentToBeRemoved){
                    throw new InternalException("this department has members in it and cannot be deleted");
                }
            }
            departmentRepository.deleteById(departmentToBeRemoved);
            return "Department removed successfully!";
        }
        else
            throw new InternalException("You're trying to delete a non existing team");
        //ekteb tests el team, department, changing password variations, scheduled task, deleting variations(removing top manager, removing non existing)
    }

}
