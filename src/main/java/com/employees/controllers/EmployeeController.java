package com.employees.controllers;


import com.employees.DTOs.EmployeeDto;
import com.employees.models.Salary;
import com.employees.services.EmployeeService;
import com.employees.services.HrService;
import com.employees.services.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/employee/") //sign in and out, view salary history
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SalaryService salaryService;

    @PostMapping(path = "/vacation")
    @ResponseBody
    public void requestVacation(@RequestBody Integer employeeId) throws Exception
    {
        int daysOff = employeeService.requestVacation(employeeId);
         }

    @GetMapping(path = "/salaryHistory")
    @ResponseBody
    public List<Salary> getSalaryHistory(@RequestBody Integer employeeId) throws Exception
    {
        List<Salary> result = salaryService.getAllSalaryHistory(employeeId);
        System.out.println("salary history: " + result);
        return result;
    }

}
