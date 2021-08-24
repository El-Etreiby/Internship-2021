package com.employees.controllers;

import com.employees.DTOs.EmployeeDto;
import com.employees.models.Employee;
import com.employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController // This means that this class is a Controller
@RequestMapping(path = "/employee") // This means URL's start with /demo (after Application path)
public class EmployeeController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private EmployeeService employeeService;


    @PostMapping(path = "") // Map ONLY POST Requests
    @ResponseBody //search
    public String addNewEmployee(@RequestBody Employee employee) throws Exception
    // @RequestParam means it is a parameter from the GET or POST request
    {
        employee.setNetSalary();
        employeeService.addNewEmployee(employee);
        return "Employee added successfully!";
    }
    @PutMapping (path = "/{employeeId}")
    @ResponseBody //search
    public String updateEmployee(@RequestBody Employee employee, @PathVariable String employeeId) throws Exception {
        EmployeeDto employeeDto = new EmployeeDto(employee);
        System.out.println("EMP: " + employeeDto);
        employeeService.updateEmployee(employeeId,employeeDto);
        return "Employee modified successfully!";
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
    @DeleteMapping(path = "/{id}/team") // Map ONLY POST Requests
    @ResponseBody //search
    public String removeEmployeesTeam(@PathVariable String id) throws Exception {
        employeeService.removeEmployeesTeam(Integer.parseInt(id));
        return "Employee's team Removed!";
    }
    @DeleteMapping(path = "/{id}/department") // Map ONLY POST Requests
    @ResponseBody //search
    public String removeEmployeesDepartment(@PathVariable String id) throws Exception {
        employeeService.removeEmployeesDepartment(Integer.parseInt(id));
        return "Employee's department Removed!";
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
    @GetMapping(path = "/{employeeId}")
    @ResponseBody
    public String getEmployee(@PathVariable String employeeId) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        String result = employeeService.getEmployee(intEmployeeId).toString();
        System.out.println(result);
        return result;
    }
    @GetMapping(path = "/all") //
    @ResponseBody
    public ArrayList<String> getAllEmployees() throws Exception {
        ArrayList<EmployeeDto> dtos = employeeService.getAllEmployees();
        return this.addDtosToArrayList(dtos);
    }

    @GetMapping(path = "/{employeeId}/salary") //
    @ResponseBody
    public String getEmployeeSalary(@PathVariable String employeeId) throws Exception {
        Integer intEmployeeId = Integer.parseInt(employeeId);
        String result = employeeService.getEmployeeSalary(intEmployeeId);
        System.out.println(result);
        return result;
    }

    @GetMapping(path = "/manager/{managerId}") //gets employees managed by "employeeId"
    @ResponseBody
    public ArrayList<String> getEmployeesUnderManager(@PathVariable String managerId) throws Exception {

        List<EmployeeDto> dtos = employeeService.getEmployeesUnderManager(Integer.parseInt(managerId));
        return this.addDtosToArrayList(dtos);
    }


    @GetMapping(path = "/manager/{managerId}/all") //gets employees managed by "employeeId"
    @ResponseBody
    public ArrayList<String> getAllEmployeesUnderManager(@PathVariable String managerId) throws Exception {

        List<EmployeeDto> dtos = employeeService.getAllEmployeesUnderManager(Integer.parseInt(managerId));
        return this.addDtosToArrayList(dtos);
    }


    @GetMapping(path = "/team/{teamId}") //gets employees managed by "employeeId"
    @ResponseBody
    public ArrayList<String> getEmployeesInTeam(@PathVariable String teamId) throws Exception {

        List<EmployeeDto> dtos = employeeService.getEmployeesInTeam(Integer.parseInt(teamId));
        return this.addDtosToArrayList(dtos);
    }

    @GetMapping(path = "/department/{departmentId}") //gets employees managed by "employeeId"
    @ResponseBody
    public ArrayList<String> getEmployeesInDepartment (@PathVariable String departmentId) throws Exception {
        List<EmployeeDto> dtos = employeeService.getEmployeesInDepartment(Integer.parseInt(departmentId));
        return this.addDtosToArrayList(dtos);
    }

    private ArrayList<String> addDtosToArrayList(List<EmployeeDto> dtos){
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < dtos.size(); i++){
            result.add(dtos.get(i).toString());
        }
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