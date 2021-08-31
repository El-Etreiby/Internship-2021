package com.employees.controllers;

import com.employees.models.Department;
import com.employees.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping(path = "/")
    @ResponseBody
    public String addNewDepartment(@RequestBody Department department){
        departmentService.addNewDepartment(department);
        return "Department Saved!";
    }
    @DeleteMapping(path = "/{departmentId}")
    @ResponseBody
    public String removeDepartment(@PathVariable String departmentId) throws Exception {
        departmentService.removeDepartment(Integer.parseInt(departmentId));
        return "Department Removed!";
    }

}
