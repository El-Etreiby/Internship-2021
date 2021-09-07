package com.employees.controllers;

import com.employees.DTOs.EmployeeDto;
import com.employees.errorHandling.BusinessException;
import com.employees.errorHandling.EmployeeNotFoundException;
import com.employees.models.*;
import com.employees.services.DepartmentService;
import com.employees.services.HrService;
import com.employees.services.SalaryService;
import com.employees.services.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(path = "/hr")
public class HrController {
    @Autowired
    private HrService hrService;

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private DepartmentService departmentService;

    private static final Logger log = LoggerFactory.getLogger(HrController.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping(path = "/employee/password/{password}")
    @ResponseBody
    public String addNewEmployee(@RequestBody Employee employee, @PathVariable String password) throws Exception  //zawed account info
    {
        log.info("provided pw: " + password);
        hrService.addNewEmployee(employee, password);
        return "Employee added successfully!";
    }

    @PutMapping(path = "/employee/{id}")
    @ResponseBody
    public String updateEmployee(@RequestBody Employee employee, @PathVariable String id) throws Exception {
        EmployeeDto employeeDto = new EmployeeDto(employee);
        log.info("dto: " + employeeDto);
        hrService.updateEmployee(id, employeeDto);
        return "Employee modified successfully!";
    }

    @DeleteMapping(path = "/employee/{id}") // Map ONLY POST Requests
    @ResponseBody //search
    public String removeEmployee(@PathVariable String id) throws Exception {
        //WHAT ABOUT FKS (manager, managed team, team....) ?????
        hrService.removeEmployee(Integer.parseInt(id));
        return "Employee Removed!";
    }

    @DeleteMapping(path = "/employee/{id}/manager") // Map ONLY POST Requests
    @ResponseBody //search
    public String removeEmployeesManager(@PathVariable String id) throws Exception {
        hrService.removeEmployeesManager(Integer.parseInt(id));
        return "Employee's manager Removed!";
    }

    @DeleteMapping(path = "/employee/{id}/team") // Map ONLY POST Requests
    @ResponseBody //search
    public String removeEmployeesTeam(@PathVariable String id) throws Exception {
        hrService.removeEmployeesTeam(Integer.parseInt(id));
        return "Employee's team Removed!";
    }

    @DeleteMapping(path = "/employee/{id}/department") // Map ONLY POST Requests
    @ResponseBody //search
    public String removeEmployeesDepartment(@PathVariable String id) throws Exception {
        hrService.removeEmployeesDepartment(Integer.parseInt(id));
        return "Employee's department Removed!";
    }

    @PostMapping(path = "/employee/{employeeId}/team/{teamId}") // Map ONLY POST Requests
    @ResponseBody
    public String addEmployeeToTeam(@PathVariable String employeeId, @PathVariable String teamId) throws Exception {
        Integer EmployeeId = Integer.parseInt(employeeId);
        Integer TeamId = Integer.parseInt(teamId);
        hrService.addEmployeeToTeam(EmployeeId, TeamId);
        return "Employee added to team!";
    }

    @PostMapping(path = "/employee/{employeeId}/department/{departmentId}") // Map ONLY POST Requests
    @ResponseBody
    public String addEmployeeToDepartment(@PathVariable String employeeId, @PathVariable String departmentId) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Integer intDepartmentId = Integer.parseInt(departmentId);
        hrService.addEmployeeToDepartment(intEmployeeId, intDepartmentId);
        return "Employee added to department!";
    }

    @PostMapping(path = "/employee/{employeeId}/manager/{managerId}") // Map ONLY POST Requests
    @ResponseBody
    public String addManagerToEmployee(@PathVariable String employeeId, @PathVariable String managerId) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Integer intManagerId = Integer.parseInt(managerId);
        hrService.addManagerToEmployee(intEmployeeId, intManagerId);
        return "Manager added to employee!";
    }

    @GetMapping(path = "/employee/{employeeId}")
    @ResponseBody
    public String getEmployee(@PathVariable String employeeId) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        String result = hrService.getEmployee(intEmployeeId).toString();
        System.out.println(result);
        return result;
    }

    @GetMapping(path = "/employee/all")
    @ResponseBody
    public ArrayList<String> getAllEmployees() {
        ArrayList<EmployeeDto> dtos = hrService.getAllEmployees();
        ArrayList<String> result = this.addDtosToArrayList(dtos);
        System.out.println(result);
        return result;
    }

    @GetMapping(path = "/employee/{employeeId}/salary")
    @ResponseBody
    public List<Salary> getEmployeeSalary(@PathVariable String employeeId) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        List<Salary> result = salaryService.getAllSalaryHistory(intEmployeeId);
        return result;
    }

    @GetMapping(path = "/employee/salary")
    @ResponseBody
    public Salary getEmployeeSalaryByDate(@RequestBody SalaryId salaryId) throws Exception {
        Salary result = salaryService.getSomeSalaryHistory(salaryId);
        return result;
    }

    @GetMapping(path = "/employeesUnderManager/{managerId}")
    @ResponseBody
    public ArrayList<String> getEmployeesUnderManager(@PathVariable String managerId) throws Exception {

        List<EmployeeDto> dtos = hrService.getEmployeesUnderManager(Integer.parseInt(managerId));
        return this.addDtosToArrayList(dtos);
    }


    @GetMapping(path = "/allEmployeesUnderManager/{managerId}")
    @ResponseBody
    public ArrayList<String> getAllEmployeesUnderManager(@PathVariable String managerId) throws Exception {

        List<EmployeeDto> dtos = hrService.getAllEmployeesUnderManager(Integer.parseInt(managerId));
        System.out.println(dtos);
        return this.addDtosToArrayList(dtos);
    }


//    @GetMapping(path = "/employeesInTeam/team/{teamId}")
//    @ResponseBody
//    public ArrayList<String> getEmployeesInTeam(@PathVariable String teamId) throws Exception {
//
//        List<EmployeeDto> dtos = hrService.getEmployeesInTeam(Integer.parseInt(teamId));
//        return this.addDtosToArrayList(dtos);
//    }
//
//    @GetMapping(path = "/employeesInDepartment/{departmentId}")
//    @ResponseBody
//    public ArrayList<String> getEmployeesInDepartment(@PathVariable String departmentId) throws Exception {
//        List<EmployeeDto> dtos = hrService.getEmployeesInDepartment(Integer.parseInt(departmentId));
//        return this.addDtosToArrayList(dtos);
//    }

    @PostMapping(path = "/team")
    @ResponseBody
    public String addNewTeam(@RequestBody Team team) {
        teamService.addNewTeam(team);
        return "Team Saved!";
    }

    @DeleteMapping(path = "/team/{id}") // Map ONLY POST Requests
    @ResponseBody
    public String removeTeam(@RequestBody Integer teamToBeRemoved) throws Exception {
        teamService.removeTeam(teamToBeRemoved);
        return "Team Removed!";
    }

    @PostMapping(path = "/department")
    @ResponseBody
    public String addNewDepartment(@RequestBody Department department) {
        departmentService.addNewDepartment(department);
        return "Department Saved!";
    }

    @DeleteMapping(path = "/department/{departmentId}")
    @ResponseBody
    public String removeDepartment(@PathVariable String departmentId) throws Exception {
        departmentService.removeDepartment(Integer.parseInt(departmentId));
        return "Department Removed!";
    }

    @PostMapping(path = "/employee/{employeeId}/bonus/{bonus}")
    @ResponseBody
    public String addBonusToEmployee(@PathVariable String employeeId, @PathVariable String bonus) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Double doubleBonus = Double.parseDouble(bonus);
        hrService.addBonusToEmployee(intEmployeeId,doubleBonus);
        return "Bonus added to employee!";
    }

    @PostMapping(path = "/employee/{employeeId}/raise/{raise}")
    @ResponseBody
    public String addRaiseToEmployee(@PathVariable String employeeId, @PathVariable String raise) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Double doubleRaise = Double.parseDouble(raise);
        hrService.addRaiseToEmployee(intEmployeeId, doubleRaise);
        return "Raise added to employee!";
    }

    private ArrayList<String> addDtosToArrayList(List<EmployeeDto> dtos) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < dtos.size(); i++) {
            result.add(dtos.get(i).toString());
        }
        return result;
    }

    @Scheduled(fixedRateString = "2628000000") //2628000000 = month
    public void issueSalaries() throws ParseException, BusinessException, EmployeeNotFoundException {
        String dateAsString = dateFormat.format(new Date());
        Date date = dateFormat.parse(dateAsString);
        hrService.issueSalaries(date.getMonth(), date.getYear());
    }
}