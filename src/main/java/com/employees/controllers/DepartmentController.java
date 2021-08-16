package com.employees.controllers;

import com.employees.models.Department;
import com.employees.services.DepartmentService;
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
        departmentService.addNewDepartment(department);
        return "Department Saved!";
    }
    @PostMapping(path = "/removeDepartment")
    @ResponseBody
    public String removeDepartment(@RequestBody Integer departmentToBeRemoved) throws Exception {
        departmentService.removeDepartment(departmentToBeRemoved);
        return "Department Removed!";
    }

}
