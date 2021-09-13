package com.employees.controllers;

import com.employees.DTOs.EmployeeDto;
import com.employees.models.*;
import com.employees.services.DepartmentService;
import com.employees.services.HrService;
import com.employees.services.SalaryService;
import com.employees.services.TeamService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/hr")
public class HrController {
    private HrService hrService;
    private SalaryService salaryService;
    private TeamService teamService;
    private DepartmentService departmentService;

    private static final Logger log = LoggerFactory.getLogger(HrController.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping(path = "/employee/password/{password}")
    @ResponseBody
    public String addNewEmployee(@RequestBody Employee employee, @PathVariable String password)   //zawed account info
    {
        log.info("provided pw: " + password);
        hrService.addNewEmployee(employee, password);
        return "Employee added successfully!";
    }

    @PutMapping(path = "/employee/{id}")
    @ResponseBody
    public String updateEmployee(@RequestBody Employee employee, @PathVariable String id) {
        EmployeeDto employeeDto = new EmployeeDto(employee);
        log.info("dto: " + employeeDto);
        hrService.updateEmployee(id, employeeDto);
        return "Employee modified successfully!";
    }

    @PutMapping(path = "/employee/{employeeId}/role")
    @ResponseBody
    public String updateEmployeesRole(@PathVariable String employeeId, @RequestBody String role) {
        hrService.updateEmployeeRole(role,Integer.parseInt(employeeId));
        return "Employee role modified successfully!";
    }

    @DeleteMapping(path = "/employee/{id}") // Map ONLY POST Requests
    @ResponseBody //search
    public String removeEmployee(@PathVariable String id) {
        //WHAT ABOUT FKS (manager, managed team, team....) ?????
        hrService.removeEmployee(Integer.parseInt(id));
        return "Employee Removed!";
    }

    @DeleteMapping(path = "/employee/{id}/manager") // Map ONLY POST Requests
    @ResponseBody //search
    public String removeEmployeesManager(@PathVariable String id) {
        hrService.removeEmployeesManager(Integer.parseInt(id));
        return "Employee's manager Removed!";
    }

    @DeleteMapping(path = "/employee/{id}/team") // Map ONLY POST Requests
    @ResponseBody //search
    public String removeEmployeesTeam(@PathVariable String id) {
        hrService.removeEmployeesTeam(Integer.parseInt(id));
        return "Employee's team Removed!";
    }

    @DeleteMapping(path = "/employee/{id}/department") // Map ONLY POST Requests
    @ResponseBody //search
    public String removeEmployeesDepartment(@PathVariable String id) {
        hrService.removeEmployeesDepartment(Integer.parseInt(id));
        return "Employee's department Removed!";
    }

    @PostMapping(path = "/employee/{employeeId}/team/{teamId}") // Map ONLY POST Requests
    @ResponseBody
    public String addEmployeeToTeam(@PathVariable String employeeId, @PathVariable String teamId) {
        Integer EmployeeId = Integer.parseInt(employeeId);
        Integer TeamId = Integer.parseInt(teamId);
        hrService.addEmployeeToTeam(EmployeeId, TeamId);
        return "Employee added to team!";
    }

    @PostMapping(path = "/employee/{employeeId}/department/{departmentId}") // Map ONLY POST Requests
    @ResponseBody
    public String addEmployeeToDepartment(@PathVariable String employeeId, @PathVariable String departmentId) {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Integer intDepartmentId = Integer.parseInt(departmentId);
        hrService.addEmployeeToDepartment(intEmployeeId, intDepartmentId);
        return "Employee added to department!";
    }

    @PostMapping(path = "/employee/{employeeId}/manager/{managerId}") // Map ONLY POST Requests
    @ResponseBody
    public String addManagerToEmployee(@PathVariable String employeeId, @PathVariable String managerId) {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Integer intManagerId = Integer.parseInt(managerId);
        hrService.addManagerToEmployee(intEmployeeId, intManagerId);
        return "Manager added to employee!";
    }

    @GetMapping(path = "/employee/{employeeId}")
    @ResponseBody
    public String getEmployee(@PathVariable String employeeId) {
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
    public List<Salary> getEmployeeSalary(@PathVariable String employeeId) {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        return salaryService.getAllSalaryHistory(intEmployeeId);
    }

    @GetMapping(path = "/employee/salary")
    @ResponseBody
    public Salary getEmployeeSalaryByDate(@RequestBody SalaryId salaryId) {
        return salaryService.getSomeSalaryHistory(salaryId);
    }

    @GetMapping(path = "/employeesUnderManager/{managerId}")
    @ResponseBody
    public ArrayList<String> getEmployeesUnderManager(@PathVariable String managerId) {

        List<EmployeeDto> dtos = hrService.getEmployeesUnderManager(Integer.parseInt(managerId));
        return this.addDtosToArrayList(dtos);
    }


    @GetMapping(path = "/allEmployeesUnderManager/{managerId}")
    @ResponseBody
    public ArrayList<String> getAllEmployeesUnderManager(@PathVariable String managerId) {

        List<EmployeeDto> dtos = hrService.getAllEmployeesUnderManager(Integer.parseInt(managerId));
        System.out.println(dtos);
        return this.addDtosToArrayList(dtos);
    }

    @PostMapping(path = "/team")
    @ResponseBody
    public String addNewTeam(@RequestBody Team team) {
        teamService.addNewTeam(team);
        return "Team Saved!";
    }

    @DeleteMapping(path = "/team") // Map ONLY POST Requests
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
    public String addBonusToEmployee(@PathVariable String employeeId, @PathVariable String bonus) {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Double doubleBonus = Double.parseDouble(bonus);
        hrService.addBonusToEmployee(intEmployeeId,doubleBonus);
        return "Bonus added to employee!";
    }

    @PostMapping(path = "/employee/{employeeId}/raise/{raise}")
    @ResponseBody
    public String addRaiseToEmployee(@PathVariable String employeeId, @PathVariable String raise) {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Double doubleRaise = Double.parseDouble(raise);
        hrService.addRaiseToEmployee(intEmployeeId, doubleRaise);
        return "Raise added to employee!";
    }

    private ArrayList<String> addDtosToArrayList(List<EmployeeDto> dtos) {
        ArrayList<String> result = new ArrayList<>();
        for (EmployeeDto dto : dtos) {
            result.add(dto.toString());
        }
        return result;
    }
}