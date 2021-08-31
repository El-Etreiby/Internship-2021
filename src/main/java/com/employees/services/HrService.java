package com.employees.services;

import com.employees.DTOs.EmployeeDto;
import com.employees.errorHandling.BusinessException;
import com.employees.errorHandling.DepartmentNotFoundException;
import com.employees.errorHandling.EmployeeNotFoundException;
import com.employees.errorHandling.TeamNotFoundException;
import com.employees.models.Department;
import com.employees.models.Employee;
import com.employees.models.Team;
import com.employees.repositories.DepartmentRepository;
import com.employees.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employees.repositories.EmployeeRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class HrService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    public String addNewEmployee(Employee employee) throws Exception {
        if (employee.getGender() != 'M' && employee.getGender() != 'F' && employee.getGender() != 'm' && employee.getGender() != 'f')
            throw new Exception("an employee's gender can only be male (represented as 'M') or female (represented as 'F')");
        String firstName;
        String lastName;
        if (employee.getFirstName() != null) {
            firstName = employee.getFirstName();
            for (int i = 0; i < firstName.length(); i++)
                if (!firstName.matches("[a-zA-Z]+"))
                    throw new Exception("an employee's first name should consist of only letters");
        }
        if (employee.getLastName() != null) {
            lastName = employee.getLastName();
            for (int i = 0; i < lastName.length(); i++)
                if (!lastName.matches("[a-zA-Z]+"))
                    throw new Exception("an employee's last name should consist of only letters");
        }
        employeeRepository.save(employee);
        return "Employee added successfully!";
    }

    public String removeEmployee(Integer employeeToBeRemoved) throws Exception {
        Optional<Employee> toBeRemoved = employeeRepository.findById(employeeToBeRemoved);
        if (!toBeRemoved.isPresent()) {
            throw new Exception("You're trying to delete a non existing employee");
        }
        if (toBeRemoved.get().getManager() == null) {
            throw new Exception("You can't delete a top manager!");
        }
        List<Employee> managedEmployees = toBeRemoved.get().getManagedEmployees();
        Employee newManager = toBeRemoved.get().getManager();
        Iterable<Employee> allEmployees = employeeRepository.findAll();
        System.out.println("All : " + allEmployees);
        for (int i = 0; i < managedEmployees.size(); i++) {
            managedEmployees.get(i).setManager(newManager);
            employeeRepository.save(managedEmployees.get(i));
        }
        employeeRepository.deleteByEmployeeId(employeeToBeRemoved);
        Iterable<Employee> remainingEmployees = employeeRepository.findAll();
        System.out.println("Removed " + employeeToBeRemoved + ". Remaining : " + remainingEmployees);
        return "Employee deleted successfully!";

    }

    public void addEmployeeToTeam(Integer employeeId, Integer teamId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        Optional<Team> team = teamRepository.findById(teamId);
        if (!toBeUpdated.isPresent()) {
            throw new Exception("This employee does not exist");
        }
        if (!team.isPresent()) {
            throw new Exception("This team does not exist");
        }
        toBeUpdated.get().setEmployeeTeam(team.get());
        employeeRepository.save(toBeUpdated.get());
    }

    public void addEmployeeToDepartment(Integer employeeId, Integer departmentId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        Optional<Department> department = departmentRepository.findById(departmentId);
        if (!department.isPresent()) {
            throw new Exception("This department does not exist");
        }
        if (!toBeUpdated.isPresent()) {
            throw new Exception("This employee does not exist");
        }
        toBeUpdated.get().setDepartment(department.get());
        employeeRepository.save(toBeUpdated.get());
    }

    public void addManagerToEmployee(Integer employeeId, Integer managerId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent()) {
            throw new Exception("This employee does not exist");
        }
        Optional<Employee> manager = employeeRepository.findById(managerId);
        if (!manager.isPresent()) {
            throw new Exception("This manager does not exist");
        }
        toBeUpdated.get().setManager(manager.get());
        employeeRepository.save(toBeUpdated.get());
    }

    @Transactional
    public void updateEmployee(String employeeId, EmployeeDto employee) throws Exception {
        //  System.out.println("updating in service! EMPLOYEE: " + employee);
        Optional<Employee> toBeUpdated = employeeRepository.findById(Integer.parseInt(employeeId));
        if (!toBeUpdated.isPresent()) {
            throw new Exception("This employee does not exist");
        }

        if (employee.getExpertise() != null) {
            toBeUpdated.get().setExpertise(employee.getExpertise());
            employeeRepository.save(toBeUpdated.get());
        }
        if (employee.getFirstName() != null) {
            toBeUpdated.get().setFirstName(employee.getFirstName());
            employeeRepository.save(toBeUpdated.get());
        }
        if (employee.getLastName() != null) {
            toBeUpdated.get().setLastName(employee.getLastName());
            employeeRepository.save(toBeUpdated.get());
        }
        if (employee.getNationalId() != null) {
            toBeUpdated.get().setNationalId(employee.getNationalId());
            employeeRepository.save(toBeUpdated.get());
        }
        if (employee.getYearsOfExperience() != null) {
            toBeUpdated.get().setYearsOfExperience(employee.getYearsOfExperience());
            employeeRepository.save(toBeUpdated.get());
        }
        if (employee.getDepartmentId() != null) {
            Optional<Department> department = departmentRepository.findById(employee.getDepartmentId());
            if (!department.isPresent()) {
                throw new DepartmentNotFoundException("You must enter a department that exists in the database. If you want to add this employee to this department insert the department in the database first!");
            }
            toBeUpdated.get().setDepartment(department.get());
            employeeRepository.save(toBeUpdated.get());
        }  //handle modifying and inserting non existing departments, teams or managers
        if (employee.getTeamId() != null) {
            Optional<Team> team = teamRepository.findById(employee.getTeamId());
            if (!team.isPresent())
                throw new TeamNotFoundException("You must enter a team that exists in the database. If you want to add this employee to this team insert the team in the database first!");
            toBeUpdated.get().setEmployeeTeam(team.get());
            employeeRepository.save(toBeUpdated.get());
        }
        if (employee.getDob() != null) {
            toBeUpdated.get().setDob(employee.getDob());
            employeeRepository.save(toBeUpdated.get());
        }
        if (employee.getGender() == 'M' || employee.getGender() == 'F' || employee.getGender() == 'f' || employee.getGender() == 'm') {
            toBeUpdated.get().setGender(employee.getGender());
            employeeRepository.save(toBeUpdated.get());
        }
        if (employee.getGraduationDate() != null) {
            toBeUpdated.get().setGraduationDate(employee.getGraduationDate());
            employeeRepository.save(toBeUpdated.get());
        }
        if (employee.getGrossSalary() != null) {
            toBeUpdated.get().setGrossSalary(employee.getGrossSalary());
            employeeRepository.save(toBeUpdated.get());
        }
        if (employee.getManagerId() != null) {
            Optional<Employee> manager = employeeRepository.findById(employee.getManagerId());
            if (!manager.isPresent())
                throw new EmployeeNotFoundException("You must enter a manager that exists in the database. If you want to add this manager to this employee insert the manager in the database first!");
            toBeUpdated.get().setManager(manager.get());
            employeeRepository.save(toBeUpdated.get());
        }
    }

    public EmployeeDto getEmployee(Integer employeeId) throws Exception {
        Optional<Employee> tuple = employeeRepository.findById(employeeId);
        if (!tuple.isPresent())
            throw new EmployeeNotFoundException("this employee does not exist!");
        return new EmployeeDto(tuple.get());

    }

    public void removeEmployeesManager(int employeeId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent())
            throw new EmployeeNotFoundException("This employee does not exist!");
        toBeUpdated.get().setManager(null);
        employeeRepository.save(toBeUpdated.get());

    }

    public ArrayList<EmployeeDto> getAllEmployees() {
        ArrayList<EmployeeDto> result = new ArrayList<>();
        Iterable<Employee> iterableTuple = employeeRepository.findAll();
        Iterator<Employee> tuple = iterableTuple.iterator();
        while (tuple.hasNext())
            result.add(new EmployeeDto(tuple.next()));
        if (result.isEmpty())
            System.out.println("There are no employees in the database!");
        return result;
    }

    public void removeEmployeesTeam(int employeeId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent())
            throw new EmployeeNotFoundException("This employee does not exist!");
        toBeUpdated.get().setEmployeeTeam(null);
        employeeRepository.save(toBeUpdated.get());
    }

    public void removeEmployeesDepartment(int employeeId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent())
            throw new EmployeeNotFoundException("This employee does not exist!");
        toBeUpdated.get().setDepartment(null);
        employeeRepository.save(toBeUpdated.get());
    }

    public String getEmployeeSalary(Integer employeeId) throws Exception {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent())
            throw new EmployeeNotFoundException("This employee does not exist!");
        if (toBeUpdated.get().getGrossSalary() == null)
            throw new Exception("This employee does not have a salary registered yet!");
        return "Employee's gross salary: " + toBeUpdated.get().getGrossSalary();


    }

    public ArrayList<EmployeeDto> getEmployeesUnderManager(Integer managerId) throws Exception {
        Optional<Employee> manager = employeeRepository.findById(managerId);
        if (!manager.isPresent())
            throw new EmployeeNotFoundException("This manager does not exist!");
        //System.out.println("Manager found! " + manager.get().getManagedEmployees());
        List<Employee> managedEmployees = manager.get().getManagedEmployees();
        ArrayList<EmployeeDto> result = new ArrayList<>();
        for (int i = 0; i < managedEmployees.size(); i++) {
            result.add(new EmployeeDto(managedEmployees.get(i)));

        }
        return result;
    }

    public List<EmployeeDto> getEmployeesInTeam(int teamId) throws Exception {
        Optional<Team> team = teamRepository.findById(teamId);
        if (!team.isPresent())
            throw new TeamNotFoundException("This team does not exist!");
        //System.out.println("Manager found! " + manager.get().getManagedEmployees());
        List<Employee> managedEmployees = team.get().getTeamMembers();
        ArrayList<EmployeeDto> result = new ArrayList<>();
        for (int i = 0; i < managedEmployees.size(); i++) {
            result.add(new EmployeeDto(managedEmployees.get(i)));

        }
        return result;
    }

    public List<EmployeeDto> getAllEmployeesUnderManager(int managerId) throws Exception {
        Optional<Employee> manager = employeeRepository.findById(managerId);
        if (!manager.isPresent())
            throw new EmployeeNotFoundException("This employee does not exist!");
        List<Employee> managedEmployees = employeeRepository.getAllManagedEmployees(managerId);
        ArrayList<EmployeeDto> result = new ArrayList<>();
        for (int i = 0; i < managedEmployees.size(); i++) {
            result.add(new EmployeeDto(managedEmployees.get(i)));
        }
        return result;
    }

    public List<EmployeeDto> getEmployeesInDepartment(int departmentId) throws Exception {
        Optional<Department> department = departmentRepository.findById(departmentId);
        if (!department.isPresent())
            throw new DepartmentNotFoundException("This department does not exist!");
        //System.out.println("Manager found! " + manager.get().getManagedEmployees());
        List<Employee> teamEmployees = department.get().getDepartmentMembers();
        ArrayList<EmployeeDto> result = new ArrayList<>();
        for (int i = 0; i < teamEmployees.size(); i++) {
            result.add(new EmployeeDto(teamEmployees.get(i)));

        }
        return result;
    }

//    public void addBonusToEmployee(Integer employeeId, Double bonus) throws EmployeeNotFoundException, BusinessException {
//        Optional<Employee> employee = employeeRepository.findById(employeeId);
//        if (!employee.isPresent()) {
//            throw new EmployeeNotFoundException("This employee does not exist!");
//        }
//        employee.get().applyBonus(bonus);
//        employeeRepository.save(employee.get());
//
//    }

    public void addRaiseToEmployee(Integer employeeId, Double raise) throws EmployeeNotFoundException, BusinessException {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (!employee.isPresent()) {
            throw new EmployeeNotFoundException("This employee does not exist!");
        }
        employee.get().raiseEmployeeSalary(raise);
        employeeRepository.save(employee.get());
    }


//        } //modification beygeely employee object
    //REST naming convention for update --> path variables
    //search for query DSL

}
