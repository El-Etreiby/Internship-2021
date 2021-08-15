package com.employees.controllers;

import com.employees.models.Department;
import com.employees.models.Employee;
import com.employees.services.DepartmentService;
import com.employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping(path = "/addNewDepartment")
    @ResponseBody
    public String addNewEmployee(@RequestBody Department department){
        departmentService.saveDepartment(department);
        return "Department Saved!";
    }


}
