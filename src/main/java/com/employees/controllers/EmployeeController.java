package com.employees.controllers;

import com.employees.DTOs.EmployeeDto;
import com.employees.models.Employee;
import com.employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController // This means that this class is a Controller
@RequestMapping(path = "/employee") // This means URL's start with /demo (after Application path)
public class EmployeeController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private EmployeeService employeeService;


    @PostMapping(path = "/") // Map ONLY POST Requests
    @ResponseBody //search
    public String addNewEmployee(@RequestBody Employee employee)
    // @RequestParam means it is a parameter from the GET or POST request
    {
        employeeService.addNewEmployee(employee);
        return "Employee added successfully!";
    }
    @PostMapping(path = "/{employeeId}") // Map ONLY POST Requests
    @ResponseBody //search
    public String updateEmployee(@RequestBody Employee employee, @PathVariable String employeeId) throws Exception {
        employeeService.updateEmployee(employeeId,employee);
        return "Employee added successfully!";
    }

    @DeleteMapping(path = "/{id}") // Map ONLY POST Requests
    @ResponseBody //search
    public String removeEmployee(@PathVariable String id) throws Exception {
        //WHAT ABOUT FKS (manager, managed team, team....) ?????
        employeeService.removeEmployee(Integer.parseInt(id));
        return "Employee Removed!";
    }
    @DeleteMapping(path = "/{id}/manager") // Map ONLY POST Requests
    @ResponseBody //search
    public String removeEmployeesManager(@PathVariable String id) throws Exception {
        employeeService.removeEmployeesManager(Integer.parseInt(id));
        return "Employee's manager Removed!";
    }

    @PostMapping(path = "/{employeeId}/team/{teamId}") // Map ONLY POST Requests
    @ResponseBody
    public String addEmployeeToTeam(@PathVariable String employeeId, @PathVariable String teamId) throws Exception {
        Integer EmployeeId = Integer.parseInt(employeeId);
        Integer TeamId = Integer.parseInt(teamId);
        employeeService.addEmployeeToTeam(EmployeeId,TeamId);
        return "Employee added to team!";
    }
    @PostMapping(path = "/{employeeId}/department/{departmentId}") // Map ONLY POST Requests
    @ResponseBody
    public String addEmployeeToDepartment(@PathVariable String employeeId, @PathVariable String departmentId) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Integer intDepartmentId = Integer.parseInt(departmentId);
        employeeService.addEmployeeToDepartment(intEmployeeId,intDepartmentId);
        return "Employee added to department!";
    }
    @PostMapping(path = "/{employeeId}/manager/{managerId}") // Map ONLY POST Requests
    @ResponseBody
    public String addManagerToEmployee(@PathVariable String employeeId, @PathVariable String managerId) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        Integer intManagerId = Integer.parseInt(managerId);
        employeeService.addManagerToEmployee(intEmployeeId,intManagerId);
        return "Manager added to employee!";
    }
    @GetMapping(path = "/{employeeId}") //
    @ResponseBody
    public String getEmployee(@PathVariable String employeeId) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        String result = employeeService.getEmployee(intEmployeeId).toString();
        System.out.println(result);
        return result;
    }
//    @DeleteMapping(path = "/team/{id}") // Map ONLY POST Requests
//    @ResponseBody
//    public String removeEmployeeFromTeam(@PathVariable String id) throws Exception {
//        Integer employeeId = IDs.get(0);
//        Integer teamId = IDs.get(1);
//        employeeService.addEmployeeToTeam(employeeId,teamId);
//        return "Employee added to team!";
//    }

//    @Modifying
//    @Query("update User u set u.active = false where u.lastLoginDate < :date")
//    void deactivateUsersNotLoggedInSince(@Param("date") LocalDate date);

//    @GetMapping(path = "/all")
//    public @ResponseBody
//    Iterable<Employee> getAllEmployees() {
//        // This returns a JSON or XML with the users
//        return employeeRepository.findAll();
//    }
//    @GetMapping(path = " /findEmployee")
//    @ResponseBody
//    public E
}