package com.employees.services;

import com.employees.DTOs.EmployeeDto;
import com.employees.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employees.repositories.EmployeeRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

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

//        } //modification beygeely employee object
        //REST naming convention for update --> path variables
        //search for query DSL

}
