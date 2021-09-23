package com.employees.controllers;

import com.employees.models.Salary;
import com.employees.models.SalaryId;
import com.employees.services.EmployeeService;
import com.employees.services.SalaryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/employee")
@AllArgsConstructor
public class EmployeeController {


    private EmployeeService employeeService;
    private SalaryService salaryService;

    @PostMapping(path = "/vacation")
    @ResponseBody
    public String requestVacation() {
        return employeeService.requestVacation();
    }

//    @PutMapping(path = "/username")
//    @ResponseBody
//    public String changeUsername(@RequestBody String newUsername) {
//        return employeeService.changeUsername(newUsername);
//    }

    @PutMapping(path = "/password")
    @ResponseBody
    public String changePassword(@RequestBody String newPassword) {
        return employeeService.changePassword(newPassword);
    }

    @GetMapping(path = "/salaryHistory")
    @ResponseBody
    public List<Salary> getSalaryHistory() {
        List<Salary> result = employeeService.getAllSalaryHistory();
        System.out.println("salary history: " + result);
        return result;
    }

    @GetMapping(path = "/salaryByDate")
    @ResponseBody
    public String getSalaryHistory(@RequestBody SalaryId salaryId) {
        return "Salary for this date: " + "\n" +  salaryService.getSomeSalaryHistory(salaryId).toString();
    }


    @GetMapping(path = "/daysOff/month/{month}/year/{year}")
    @ResponseBody
    public String getDaysOffByDate(@PathVariable String month, @PathVariable String year ) {
         int daysOff = employeeService.getDaysOffByDate(month, year);
        System.out.println("days of taken in month: " + month + ", year: " + year + " = " + daysOff);
        return "days of taken in month: " + month + ", year: " + year + " = " + daysOff;
    }

    @GetMapping(path = "/daysOff")
    @ResponseBody
    public String getDaysOff() {
        int daysOff = employeeService.getDaysOff();
        return "days of taken this year: " + daysOff;
    }

}
