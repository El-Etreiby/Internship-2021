package com.employees.repositories;

import com.employees.errorHandling.InternalException;
import com.employees.models.AccountInformation;
import com.employees.models.Degree;
import com.employees.models.Employee;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
@AllArgsConstructor
public class DbInit implements CommandLineRunner {
    private EmployeeRepository employeeRepository;
    private AccountInformationRepository accountInformationRepository;


    @Override
    public void run(String... args) throws InternalException, ParseException {
        accountInformationRepository.deleteAll();
        employeeRepository.deleteAll();
        Employee admin = new Employee();
        admin.setFirstName("admin");
        admin.setGrossSalary(13000.0);
        admin.setLastName("admin");
        admin.setBonus(0.0);
        admin.setGender("m");
        admin.setDegree(Degree.SENIOR);
        admin.setNationalId(1000L);
        admin.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/1980"));
        employeeRepository.save(admin);
        AccountInformation accountInformation = new AccountInformation();
        accountInformation.setEmployee(admin);
        accountInformation.setPassword("123");
        accountInformation.setRole("ADMIN");
        accountInformation.setUsername("a.a");
//        Optional<Employee> emp = employeeRepository.findById(9);
//        System.out.println("present: " + emp.isPresent());
        accountInformationRepository.save(accountInformation);

        Employee employee = new Employee();
        employee.setFirstName("employee");
        employee.setGrossSalary(10000.0);
        employee.setLastName("employee");
        employee.setBonus(0.0);
        employee.setGender("m");
        employee.setDegree(Degree.FRESH);
        employee.setNationalId(1009L);
        employee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/1988"));
        employeeRepository.save(employee);
        AccountInformation accountInformation1 = new AccountInformation();
        accountInformation1.setEmployee(employee);
        accountInformation1.setUsername("b.b");
        accountInformation1.setPassword("123");
        accountInformation1.setRole("EMPLOYEE");
        accountInformationRepository.save(accountInformation1);
    }
}
