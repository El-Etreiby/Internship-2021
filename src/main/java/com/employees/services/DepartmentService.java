package com.employees.services;


import com.employees.models.Department;
import com.employees.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employees.repositories.DepartmentRepository;

import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public void saveDepartment(Department department){
        departmentRepository.save(department);
    }

    public void removeDepartment(Integer departmentToBeRemoved) throws Exception {
        Optional<Department> toBeRemoved = departmentRepository.findById(departmentToBeRemoved);
        if (toBeRemoved.isPresent())
            departmentRepository.deleteById(departmentToBeRemoved);
        else
            throw new Exception("You're trying to delete a non existing department");
    }

}
