package com.employees.controllers;

import com.employees.models.Department;
import com.employees.services.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/department")
public class DepartmentController {


    private DepartmentService departmentService;

    @PostMapping(path = "")
    @ResponseBody
    public String addNewDepartment(@RequestBody Department department){
        departmentService.addNewDepartment(department);
        return "Department Saved!";
    }
    @DeleteMapping(path = "")
    @ResponseBody
    public String removeDepartment(@RequestBody Integer departmentId) throws Exception {
        departmentService.removeDepartment(departmentId);
        return "Department Removed!";
    }

    @PutMapping(path = "/{departmentId}")
    @ResponseBody
    public String updateDepartment(@PathVariable String departmentId, @RequestBody String newName) {
        departmentService.updateDepartment(newName,Integer.parseInt(departmentId));
        return "Department Updated!";
    }

}
