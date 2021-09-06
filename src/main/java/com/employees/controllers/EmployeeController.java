package com.employees.controllers;

import com.employees.models.Salary;
import com.employees.models.SalaryId;
import com.employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(path = "/vacation")
    @ResponseBody
    public void requestVacation(@RequestBody Integer employeeId) throws Exception {
        int daysOff = employeeService.requestVacation(employeeId);
    }

    @PutMapping(path = "/username")
    @ResponseBody
    public String changeUsername(@RequestBody String newUsername) throws Exception {
        return employeeService.changeUsername(newUsername);
    }

    @PutMapping(path = "/password")
    @ResponseBody
    public String changePassword(@RequestBody String newPassword) throws Exception {
        return employeeService.changePassword(newPassword);
    }

    @GetMapping(path = "/salaryHistory")
    @ResponseBody
    public List<Salary> getSalaryHistory() throws Exception {
        List<Salary> result = employeeService.getAllSalaryHistory();
        System.out.println("salary history: " + result);
        return result;
    }

    @GetMapping(path = "/salary")
    @ResponseBody
    public Salary getSalary(@RequestBody SalaryId salaryId) throws Exception {
        Salary result = employeeService.getSomeSalaryHistory(salaryId);
        System.out.println("salary history for this date: " + result);
        return result;
    }

    @GetMapping(path = "/daysOff/month/{month}/year/{year}")
    @ResponseBody
    public String getDaysOffByDate(@PathVariable String month, @PathVariable String year ) throws Exception {
         int daysOff = employeeService.getDaysOffByDate(month, year);
        System.out.println("days of taken in month: " + month + ", year: " + year + " = " + daysOff);
        return "days of taken in month: " + month + ", year: " + year + " = " + daysOff;
    }

}
