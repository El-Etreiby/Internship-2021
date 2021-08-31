package com.employees.controllers;

import com.employees.DTOs.EmployeeDto;
import com.employees.models.*;
import com.employees.services.DepartmentService;
import com.employees.services.HrService;
import com.employees.services.SalaryService;
import com.employees.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

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

    @PostMapping(path = "/employee")
    @ResponseBody
    public String addNewEmployee(@RequestBody Employee employee) throws Exception
    {
        hrService.addNewEmployee(employee);
        return "Employee added successfully!";
    }

    @PutMapping (path = "/employee/{id}")
    @ResponseBody
    public String updateEmployee(@RequestBody Employee employee, @PathVariable String id) throws Exception {
        EmployeeDto employeeDto = new EmployeeDto(employee);
        hrService.updateEmployee(id,employeeDto);
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
        hrService.addEmployeeToTeam(EmployeeId,TeamId);
        return "Employee added to team!";
    }
    @PostMapping(path = "/employee/{employeeId}/department/{departmentId}") // Map ONLY POST Requests
    @ResponseBody
    public String addEmployeeToDepartment(@PathVariable String employeeId, @PathVariable String departmentId) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Integer intDepartmentId = Integer.parseInt(departmentId);
        hrService.addEmployeeToDepartment(intEmployeeId,intDepartmentId);
        return "Employee added to department!";
    }
    @PostMapping(path = "/employee/{employeeId}/manager/{managerId}") // Map ONLY POST Requests
    @ResponseBody
    public String addManagerToEmployee(@PathVariable String employeeId, @PathVariable String managerId) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Integer intManagerId = Integer.parseInt(managerId);
        hrService.addManagerToEmployee(intEmployeeId,intManagerId);
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
    public ArrayList<String> getAllEmployees()  {
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

    @GetMapping(path = "/employee/{employeeId}/salary/month/{month}/year/{year}")
    @ResponseBody
    public Salary getEmployeeSalaryByDate(@PathVariable String employeeId, @PathVariable String month, @PathVariable String year) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        SalaryId salaryId = new SalaryId();
        salaryId.setEmployeeId(intEmployeeId);
        salaryId.setMonth(Integer.parseInt(month));
        salaryId.setYear(Integer.parseInt(year));
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


    @GetMapping(path = "/employeesInTeam/team/{teamId}")
    @ResponseBody
    public ArrayList<String> getEmployeesInTeam(@PathVariable String teamId) throws Exception {

        List<EmployeeDto> dtos = hrService.getEmployeesInTeam(Integer.parseInt(teamId));
        return this.addDtosToArrayList(dtos);
    }

    @GetMapping(path = "/employeesInDepartment/{departmentId}")
    @ResponseBody
    public ArrayList<String> getEmployeesInDepartment (@PathVariable String departmentId) throws Exception {
        List<EmployeeDto> dtos = hrService.getEmployeesInDepartment(Integer.parseInt(departmentId));
        return this.addDtosToArrayList(dtos);
    }

    @PostMapping(path = "/team")
    @ResponseBody
    public String addNewTeam(@RequestBody Team team)
    {
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
    public String addNewDepartment(@RequestBody Department department){
        departmentService.addNewDepartment(department);
        return "Department Saved!";
    }
    @DeleteMapping(path = "/department/{departmentId}")
    @ResponseBody
    public String removeDepartment(@PathVariable String departmentId) throws Exception {
        departmentService.removeDepartment(Integer.parseInt(departmentId));
        return "Department Removed!";
    }

//    @PostMapping(path = "/employee/{employeeId}/bonus/{bonus}")
//    @ResponseBody
//    public String addBonusToEmployee(@PathVariable String employeeId, @PathVariable String bonus) throws Exception {
//        Integer intEmployeeId = Integer.parseInt(employeeId);
//        Double doubleBonus = Double.parseDouble(bonus);
//        HrService.addBonusToEmployee(intEmployeeId,doubleBonus);
//        return "Bonus added to employee!";
//    }

    @PostMapping(path = "/employee/{employeeId}/raise/{raise}")
    @ResponseBody
    public String addRaiseToEmployee(@PathVariable String employeeId, @PathVariable String raise) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Double doubleRaise = Double.parseDouble(raise);
        hrService.addRaiseToEmployee(intEmployeeId,doubleRaise);
        return "Raise added to employee!";
    }

    private ArrayList<String> addDtosToArrayList(List<EmployeeDto> dtos){
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < dtos.size(); i++){
            result.add(dtos.get(i).toString());
        }
        return result;
    }
}