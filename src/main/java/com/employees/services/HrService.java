package com.employees.services;

import com.employees.DTOs.EmployeeDto;
import com.employees.errorHandling.*;
import com.employees.models.*;
import com.employees.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class HrService {

    @Autowired
    SalaryRepository salaryRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AccountInformationRepository accountInformationRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    public void addNewEmployee(Employee employee, String providedPassword) throws BadArgumentException {
        if ( !(employee.getGender().equals("male") || employee.getGender().equals("female")) ) {
            throw new BadArgumentException("an employee's gender can only be male (represented as 'male') or female (represented as 'female')");
        }
        String firstName;
        String lastName;
        if (employee.getFirstName() != null) {
            firstName = employee.getFirstName();
            for (int i = 0; i < firstName.length(); i++)
                if (!firstName.matches("[a-zA-Z]+"))
                    throw new BadArgumentException("an employee's first name should consist of only letters");
        }
        if (employee.getLastName() != null) {
            lastName = employee.getLastName();
            for (int i = 0; i < lastName.length(); i++)
                if (!lastName.matches("[a-zA-Z]+"))
                    throw new BadArgumentException("an employee's last name should consist of only letters");
        }
        if(employee.getNationalId()==null){
            throw new BadArgumentException("insert a value for the national ID");
        }
        if (employee.getNationalId() < 0) {
            throw new BadArgumentException("invalid entry for the national ID");
        }
        if(employee.getDob()==null){
            throw new BadArgumentException("insert a value for the DoB");
        }
        if (employee.getDob().getYear() < 0 || employee.getDob().getYear() > 121) {
            throw new BadArgumentException("Enter a value less than 2021 or more than 1900 for the date of birth");
        }
        if(employee.getGrossSalary() == null){
            throw new BadArgumentException("enter a value for the gross salary");
        }
        Double minimumWage = 588.235294118;
        if(employee.getGrossSalary() < minimumWage){
            throw new BadArgumentException("the minimum employee wage is $589");
        }
        if(employee.getGraduationDate()!=null) {
            if (employee.getGraduationDate().getYear() < 0 || employee.getGraduationDate().getYear() > 121) {
                throw new BadArgumentException("Enter a value less than 2021 or more than 1900 for the date of birth ");
            }
        }
        AccountInformation accountInformation = new AccountInformation();
        String username = "";
        accountInformation.setEmployee(employee);
        if (employee.getFirstName() != null && employee.getLastName() != null) {
            username += employee.getFirstName();
            username += ".";
            username += employee.getLastName();
        }
        Optional<AccountInformation> duplicateAccount = accountInformationRepository.findByUsername(username);
        if (duplicateAccount.isPresent()) {
            int index = 0;
            while (duplicateAccount.isPresent()) {
                index++;
                duplicateAccount = accountInformationRepository.findByUsername(username + index);
            }
            username = username + index;
        }
//        Random random = new Random();
//        int leftLimit=48; // number 0
//        int rightLimit=122; //letter z
//        int targetStringLength = 10;
//        password = random.ints(leftLimit, rightLimit + 1)
//                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
//                .limit(targetStringLength)
//                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//                .toString();
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encryptedPassword = encoder.encode(providedPassword);

        System.out.println("Your generated username is: " + username);
        System.out.println("Your generated password is: " + providedPassword + " encrypted: " + encryptedPassword);
        System.out.println("Usernames and passwords can be updated through the website");
        accountInformation.setPassword(encryptedPassword);
        accountInformation.setRole("EMPLOYEE");
        accountInformation.setUsername(username);
        employeeRepository.save(employee);
        accountInformation.setEmployee(employee);
        System.out.println("Creating and saving employee " + employee);
        accountInformationRepository.save(accountInformation);
    }

    public void removeEmployee(Integer employeeToBeRemoved) throws EmployeeNotFoundException {
        Optional<Employee> toBeRemoved = employeeRepository.findById(employeeToBeRemoved);
        if (!toBeRemoved.isPresent()) {
            throw new EmployeeNotFoundException("You're trying to delete a non existing employee");
        }
        if (toBeRemoved.get().getManager() == null) {
            throw new EmployeeNotFoundException("You can't delete a top manager");
        }
        List<Employee> managedEmployees = toBeRemoved.get().getManagedEmployees();
        Employee newManager = toBeRemoved.get().getManager();
        Iterable<Employee> allEmployees = employeeRepository.findAll();
        System.out.println("All : " + allEmployees);
        for (Employee managedEmployee : managedEmployees) {
            managedEmployee.setManager(newManager);
            employeeRepository.save(managedEmployee);
        }
        employeeRepository.deleteByEmployeeId(employeeToBeRemoved);
        Optional<AccountInformation> toBeDeleted = accountInformationRepository.findByEmployeeId(employeeToBeRemoved);
        if(toBeDeleted.isPresent()) {
            accountInformationRepository.deleteByUsername(toBeDeleted.get().getUsername());
        }

    }

    public void addEmployeeToTeam(Integer employeeId, Integer teamId) throws EmployeeNotFoundException, TeamNotFoundException {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        Optional<Team> team = teamRepository.findById(teamId);
        if (!toBeUpdated.isPresent()) {
            throw new EmployeeNotFoundException("This employee does not exist");
        }
        if (!team.isPresent()) {
            throw new TeamNotFoundException("This team does not exist");
        }
        toBeUpdated.get().setEmployeeTeam(team.get());
        employeeRepository.save(toBeUpdated.get());
    }

    public void addEmployeeToDepartment(Integer employeeId, Integer departmentId) throws DepartmentNotFoundException,  EmployeeNotFoundException{
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        Optional<Department> department = departmentRepository.findById(departmentId);
        if (!department.isPresent()) {
            throw new DepartmentNotFoundException("This department does not exist");
        }
        if (!toBeUpdated.isPresent()) {
            throw new EmployeeNotFoundException("This employee does not exist");
        }
        toBeUpdated.get().setDepartment(department.get());
        employeeRepository.save(toBeUpdated.get());
    }

    public void addManagerToEmployee(Integer employeeId, Integer managerId) throws EmployeeNotFoundException {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent()) {
            throw new EmployeeNotFoundException("This employee does not exist");
        }
        Optional<Employee> manager = employeeRepository.findById(managerId);
        if (!manager.isPresent()) {
            throw new EmployeeNotFoundException("This manager does not exist");
        }
        toBeUpdated.get().setManager(manager.get());
        employeeRepository.save(toBeUpdated.get());
    }

    @Transactional
    public void updateEmployee(String employeeId, EmployeeDto employee) throws EmployeeNotFoundException {
        log.info("updating in service! EMPLOYEE: " + employee);
        Optional<Employee> toBeUpdated = employeeRepository.findById(Integer.parseInt(employeeId));
        if (!toBeUpdated.isPresent()) {
            throw new EmployeeNotFoundException("This employee does not exist");
        }
        Employee modified = toBeUpdated.get();
        if (employee.getExpertise() != null) {
            modified.setExpertise(employee.getExpertise());
        }
        if (employee.getFirstName() != null) {
            String firstName = employee.getFirstName();
            for (int i = 0; i < firstName.length(); i++)
                if (!firstName.matches("[a-zA-Z]+"))
                    throw new BadArgumentException("an employee's first name should consist of only letters");
            modified.setFirstName(employee.getFirstName());
        }
        if (employee.getLastName() != null) {
            String lastName = employee.getLastName();
            for (int i = 0; i < lastName.length(); i++)
                if (!lastName.matches("[a-zA-Z]+"))
                    throw new BadArgumentException("an employee's last name should consist of only letters");
            modified.setLastName(employee.getLastName());
        }
        if(employee.getLastName() != null || employee.getFirstName() != null){
            log.info("updating employee's username...");
            updateUsername(employee, employeeId);
        }
        if (employee.getNationalId() != null) {
            if (employee.getNationalId() < 0) {
                throw new BadArgumentException("invalid entry for the national ID");
            }
            modified.setNationalId(employee.getNationalId());
        }
        if (employee.getDepartmentId() != null) {
            log.info("department != null");
            Optional<Department> department = departmentRepository.findById(employee.getDepartmentId());
            if (!department.isPresent()) {
                throw new DepartmentNotFoundException("You must enter a department that exists in the database. If you want to add this employee to this department insert the department in the database first");
            }
            modified.setDepartment(department.get());
        }  //handle modifying and inserting non existing departments, teams or managers
        if (employee.getTeamId() != null) {
            Optional<Team> team = teamRepository.findById(employee.getTeamId());
            if (!team.isPresent())
                throw new TeamNotFoundException("You must enter a team that exists in the database. If you want to add this employee to this team insert the team in the database first");
            modified.setEmployeeTeam(team.get());
        }
        if (employee.getDob() != null) {
            if (employee.getDob().getYear() < 0 || employee.getDob().getYear() > 121) {
                throw new BadArgumentException("Enter a value less than 2021 or more than 1900 for the date of birth ");
            }
            modified.setDob(employee.getDob());
        }
        if(employee.getGender()!=null) {
            if (employee.getGender().equals("male") || employee.getGender().equals("female")) {
                modified.setGender(employee.getGender());
            } else {
                throw new BadArgumentException("an employee's gender can only be male (represented as 'male') or female (represented as 'female')");
            }
        }
        if (employee.getGraduationDate() != null) {
            if (employee.getGraduationDate().getYear() < 1900 || employee.getGraduationDate().getYear() > 2021) {
                throw new BadArgumentException("Enter a value less than 2021 or more than 1900 for the date of graduation ");
            }
            modified.setGraduationDate(employee.getGraduationDate());
        }
        log.info("Gross salary: " + employee.getGrossSalary());
        if (employee.getGrossSalary() != null) {
            Double minimumWage = 588.235294118;
        if(employee.getGrossSalary() <  minimumWage) {
            throw new BadArgumentException("the minimum employee wage is $589");
        }
        modified.setGrossSalary(employee.getGrossSalary());
        }
        if (employee.getManagerId() != null) {
            Optional<Employee> manager = employeeRepository.findById(employee.getManagerId());
            if (!manager.isPresent())
                throw new EmployeeNotFoundException("You must enter a manager that exists in the database. If you want to add this manager to this employee insert the manager in the database first");
            modified.setManager(manager.get());
        }
        log.info("savinf modifiedd: " + modified);
        employeeRepository.save(modified);
    }

    private void updateUsername(EmployeeDto employee, String employeeId) {
        log.info("updating username...");
        Optional<AccountInformation> account = accountInformationRepository.findByEmployeeId(Integer.parseInt(employeeId));
        AccountInformation toBeUpdated = account.get();
        String username = "";
        Optional<Employee> emp = employeeRepository.findById(Integer.parseInt(employeeId));
        if(employee.getFirstName()==null){
            employee.setFirstName(emp.get().getFirstName());
        }
        if(employee.getLastName()==null){
            employee.setLastName(emp.get().getLastName());
        }
        if (employee.getFirstName() != null && employee.getLastName() != null) {
            username += employee.getFirstName();
            username += ".";
            username += employee.getLastName();
        }
        Optional<AccountInformation> duplicateAccount = accountInformationRepository.findByUsername(username);
        if (duplicateAccount.isPresent()) {
            int index = 0;
            while (duplicateAccount.isPresent()) {
                index++;
                duplicateAccount = accountInformationRepository.findByUsername(username + index);
            }
            username = username + index;
        }
        toBeUpdated.setUsername(username);
        log.info("username updated: " + username);
    }

    public EmployeeDto getEmployee(Integer employeeId) {
        Optional<Employee> tuple = employeeRepository.findById(employeeId);
        if (!tuple.isPresent())
            throw new EmployeeNotFoundException("this employee does not exist");
        return new EmployeeDto(tuple.get());

    }

    public void removeEmployeesManager(int employeeId) {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent())
            throw new EmployeeNotFoundException("This employee does not exist");
        toBeUpdated.get().setManager(null);
        employeeRepository.save(toBeUpdated.get());

    }

    public ArrayList<EmployeeDto> getAllEmployees() {
        ArrayList<EmployeeDto> result = new ArrayList<>();
        Iterable<Employee> iterableTuple = employeeRepository.findAll();
        for (Employee employee : iterableTuple){
            result.add(new EmployeeDto(employee));
        }
        if (result.isEmpty())
            System.out.println("There are no employees in the database");
        return result;
    }

    public void removeEmployeesTeam(int employeeId) {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent())
            throw new EmployeeNotFoundException("This employee does not exist");
        toBeUpdated.get().setEmployeeTeam(null);
        employeeRepository.save(toBeUpdated.get());
    }

    public void removeEmployeesDepartment(int employeeId) {
        Optional<Employee> toBeUpdated = employeeRepository.findById(employeeId);
        if (!toBeUpdated.isPresent())
            throw new EmployeeNotFoundException("This employee does not exist");
        toBeUpdated.get().setDepartment(null);
        employeeRepository.save(toBeUpdated.get());
    }

    public ArrayList<EmployeeDto> getEmployeesUnderManager(Integer managerId) {
        Optional<Employee> manager = employeeRepository.findById(managerId);
        if (!manager.isPresent())
            throw new EmployeeNotFoundException("This manager does not exist");
        //System.out.println("Manager found! " + manager.get().getManagedEmployees());
        List<Employee> managedEmployees = manager.get().getManagedEmployees();
        ArrayList<EmployeeDto> result = new ArrayList<>();
        for (Employee managedEmployee : managedEmployees) {
            result.add(new EmployeeDto(managedEmployee));

        }
        return result;
    }

    public List<EmployeeDto> getAllEmployeesUnderManager(int managerId) {
        Optional<Employee> manager = employeeRepository.findById(managerId);
        if (!manager.isPresent())
            throw new EmployeeNotFoundException("This employee does not exist");
        List<Employee> managedEmployees = employeeRepository.getAllManagedEmployees(managerId);
        ArrayList<EmployeeDto> result = new ArrayList<>();
        for (Employee managedEmployee : managedEmployees) {
            result.add(new EmployeeDto(managedEmployee));
        }
        return result;
    }

    public void addBonusToEmployee(Integer employeeId, Double bonus) throws EmployeeNotFoundException, InternalException {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (!employee.isPresent()) {
            throw new EmployeeNotFoundException("This employee does not exist");
        }
        employee.get().setBonus(bonus);
        employeeRepository.save(employee.get());

    }

    public void addRaiseToEmployee(Integer employeeId, Double raise) throws EmployeeNotFoundException, BadArgumentException {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (!employee.isPresent()) {
            throw new EmployeeNotFoundException("This employee does not exist");
        }
        Employee toBeUpdated = employee.get();
        if(raise < 0){
            throw new BadArgumentException("a raise must be of a positive value");
        }
        toBeUpdated.setRaise(raise);
        employeeRepository.save(toBeUpdated);
    }

    public void issueSalaries(int month, int year) {
        Iterable<Employee> employee = employeeRepository.findAll();
        Iterator<Employee> employees = employee.iterator();
        Employee temp;
        while (employees.hasNext()) {
            temp = employees.next();
            SalaryId salaryId = new SalaryId();
            salaryId.setEmployee_id(temp.getEmployeeId());
            salaryId.setMonth(month);
            salaryId.setYear(year);
            Salary newSalary = new Salary();
            newSalary.setEmployee(temp);
            newSalary.setId(salaryId);
            newSalary.calculateNetSalary();
            salaryRepository.save(newSalary);
            temp.setBonus(0.0);
            temp.setGrossSalary(temp.getGrossSalary() + temp.getRaise());
            temp.setRaise(0.0);
            if (temp.getYearsOfExperience() < 10 && temp.getDaysOffTaken() > 21) {
                temp.setDaysOffTaken(21);
            }
            if (temp.getYearsOfExperience() > 10 && temp.getDaysOffTaken() > 30) {
                temp.setDaysOffTaken(30);
            }
            if (month == 12) {
                temp.setDaysOffTaken(0);
                temp.setYearsOfExperience(temp.getYearsOfExperience() + 1);
            }
            employeeRepository.save(temp);
        }

    }

    public void updateEmployeeRole(String role, int employeeId) {
        Optional<AccountInformation> toBeUpdated = accountInformationRepository.findByEmployeeId(employeeId);
        if(role.length() ==0 ){
            throw new BadArgumentException("insert a value for the role");
        }
        if(role.charAt(0)=='"'){
            role = role.substring(1);
        }
        if(role.charAt(role.length()-1)=='"'){
            role = role.substring(0, role.length()-1);
        }
        if(!toBeUpdated.isPresent()){
            throw new EmployeeNotFoundException("This employee does not exist");
        }
        if(!role.equals("EMPLOYEE") && !role.equals("ADMIN")){
            throw new BadArgumentException("'EMPLOYEE' and 'ADMIN' are the only valid roles in the system");
        }
        AccountInformation account = toBeUpdated.get();
        account.setRole(role);
        accountInformationRepository.save(account);
    }


//        } //modification beygeely employee object
    //REST naming convention for update --> path variables
    //search for query DSL

}
