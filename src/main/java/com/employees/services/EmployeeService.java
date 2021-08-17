package com.employees.services;

import com.employees.DTOs.EmployeeDto;
import com.employees.models.Employee;
import com.employees.models.Team;
import com.employees.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employees.repositories.EmployeeRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TeamRepository teamRepository;

    public String addNewEmployee(Employee employee) {
        employeeRepository.save(employee);
        return "Employee added successfully!";
    }

    public String removeEmployee(Integer employeeToBeRemoved) throws Exception {
        Optional<Employee> toBeRemoved = employeeRepository.findById(employeeToBeRemoved);
        if (toBeRemoved.isPresent()) {
            employeeRepository.deleteById(employeeToBeRemoved);
            return "Employee deleted successfully!";
        }
        else
            throw new Exception("You're trying to delete a non existing employee");


    }

    public static <T> Iterable<T>
    getIterableFromIterator(Iterator<T> iterator) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }

    public void addEmployeeToTeam(Integer employeeId, Integer teamId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent()) {
            throw new Exception("This employee does not exist");
        }
        employeeRepository.addEmployeeToTeam(employeeId,teamId);
    }

    public void addEmployeeToDepartment(Integer employeeId, Integer departmentId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent()) {
            throw new Exception("This employee does not exist");
        }
        employeeRepository.addEmployeeToDepartment(employeeId,departmentId);
    }

    public void addManagerToEmployee(Integer employeeId, Integer managerId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent()) {
            throw new Exception("This employee does not exist");
        }
        employeeRepository.addManagerToEmployee(employeeId,managerId);
    }

    public void updateEmployee(String employeeId, Employee employee) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(Integer.parseInt(employeeId));
        if (!toBeUpdated.isPresent()) {
            throw new Exception("This employee does not exist");
        }
        if(employee.getEmployeeName()!=null){
            employeeRepository.updateEmployeeName(Integer.parseInt(employeeId),employee.getEmployeeName());
        }
//        if(employee.getDepartment()!=null){
//            employeeRepository.updateEmployeeDepartment(Integer.parseInt(employeeId),employee.getDepartment());
//        }
//        if(employee.getEmployeeTeam()!=null){
//            employeeRepository.updateEmployeeTeam(Integer.parseInt(employeeId),employee.getEmployeeTeam());
//        }
        if(employee.getDob()!=null){
            employeeRepository.updateEmployeeDob(Integer.parseInt(employeeId),employee.getDob());
        }
        if(employee.getGender()=='M'||employee.getGender()=='F'){
            employeeRepository.updateEmployeeGender(Integer.parseInt(employeeId),employee.getGender());
        }
        if(employee.getGraduationDate()!=null){
            employeeRepository.updateEmployeeGraduationDate(Integer.parseInt(employeeId),employee.getGraduationDate());
        }
        if(employee.getGrossSalary()!=null){
            employeeRepository.updateEmployeeGrossSalary(Integer.parseInt(employeeId),employee.getGrossSalary());
        }
        if(employee.getNetSalary()!=null){
            employeeRepository.updateEmployeeNetSalary(Integer.parseInt(employeeId),employee.getNetSalary());
        }
//        if(employee.getManager()!=null){
//            employeeRepository.updateEmployeeManager(Integer.parseInt(employeeId),employee.getManager());
//        }
//        if(employee.getManagedEmployees()!=null){
//            employeeRepository.updateEmployeeManagedEmployees(Integer.parseInt(employeeId),employee.getManagedEmployees());
//        }


    }

    public EmployeeDto getEmployee(Integer employeeId) throws Exception {
       Optional<Employee> tuple = employeeRepository.findById(employeeId);
       if(!tuple.isPresent())
           throw new Exception("this employee does not exist!");
       return new EmployeeDto(tuple.get());

    }

    public void removeEmployeesManager(int employeeId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if(!toBeUpdated.isPresent())
            throw new Exception("This employee does not exist!");
        employeeRepository.removeEmployeesManager(employeeId);

    }

    public ArrayList<EmployeeDto> getAllEmployees() {
        ArrayList<EmployeeDto> result = new ArrayList<EmployeeDto>();
        Iterable<Employee> iterableTuple = employeeRepository.findAll();
        Iterator<Employee> tuple = iterableTuple.iterator();
        while(tuple.hasNext())
            result.add(new EmployeeDto(tuple.next()));
        return result;
    }

    public void removeEmployeesTeam(int employeeId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if(!toBeUpdated.isPresent())
            throw new Exception("This employee does not exist!");
        employeeRepository.removeEmployeesTeam(employeeId);
    }

    public void removeEmployeesDepartment(int employeeId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if(!toBeUpdated.isPresent())
            throw new Exception("This employee does not exist!");
        employeeRepository.removeEmployeesDepartment(employeeId);
    }

    public String getEmployeeSalary(Integer employeeId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if(!toBeUpdated.isPresent())
            throw new Exception("This employee does not exist!");
        return "Employee's gross salary: " + toBeUpdated.get().getGrossSalary()
                + ". After taxes and deductions his net salary becomes: " + toBeUpdated.get().getNetSalary();

    }

    public ArrayList<EmployeeDto> getEmployeesUnderManager(Integer employeeId) throws Exception {
        Optional<Employee> manager = employeeRepository.findById(employeeId);
        if(!manager.isPresent())
            throw new Exception("This manager does not exist!");
        //System.out.println("Manager found! " + manager.get().getManagedEmployees());
        List<Employee> managedEmployees = manager.get().getManagedEmployees();
        ArrayList<EmployeeDto> result = new ArrayList<EmployeeDto>();
        for(int i = 0 ; i < managedEmployees.size(); i++){
            result.add(new EmployeeDto(managedEmployees.get(i)));

        }
        return result;
    }

    public List<EmployeeDto> getEmployeesInTeam(int teamId) throws Exception {
        Optional<Team> team = teamRepository.findById(teamId);
        if(!team.isPresent())
            throw new Exception("This manager does not exist!");
        //System.out.println("Manager found! " + manager.get().getManagedEmployees());
        List<Employee> managedEmployees = team.get().getTeamMembers();
        ArrayList<EmployeeDto> result = new ArrayList<EmployeeDto>();
        for(int i = 0 ; i < managedEmployees.size(); i++){
            result.add(new EmployeeDto(managedEmployees.get(i)));

        }
        return result;
    }

//        } //modification beygeely employee object
        //REST naming convention for update --> path variables
        //search for query DSL

}
