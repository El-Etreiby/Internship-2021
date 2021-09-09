//package com.employees.repositories;
//
//import com.employees.errorHandling.InternalException;
//import com.employees.models.AccountInformation;
//import com.employees.models.Degree;
//import com.employees.models.Employee;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Service;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//
//@Service
//public class DbInit implements CommandLineRunner {
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Autowired
//    private AccountInformationRepository accountInformationRepository;
//
//
//    @Override
//    public void run(String... args) throws InternalException, ParseException {
//        accountInformationRepository.deleteAll();
//        employeeRepository.deleteAll();
//        Employee admin = new Employee();
//        admin.setFirstName("admin");
//        admin.setGrossSalary(13000.0);
//        admin.setLastName("admin");
//        admin.setBonus(0.0);
//        admin.setGender('M');
//        admin.setDegree(Degree.SENIOR);
//        admin.setNationalId(1000L);
//        admin.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/1980"));
//        employeeRepository.save(admin);
//        AccountInformation accountInformation = new AccountInformation();
//        accountInformation.setEmployee(admin);
//        accountInformation.setUsername("admin");
//        accountInformation.setPassword("admin123");
//        accountInformation.setRole("ADMIN");
//        accountInformationRepository.save(accountInformation);
//
//        Employee employee = new Employee();
//        employee.setFirstName("employee");
//        employee.setGrossSalary(10000.0);
//        employee.setLastName("employee");
//        employee.setBonus(0.0);
//        employee.setGender('M');
//        employee.setDegree(Degree.FRESH);
//        employee.setNationalId(1009L);
//        employee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/1988"));
//        employeeRepository.save(employee);
//        AccountInformation accountInformation1 = new AccountInformation();
//        accountInformation1.setEmployee(admin);
//        accountInformation1.setUsername("employee");
//        accountInformation1.setPassword("employee123");
//        accountInformation1.setRole("EMPLOYEE");
//        accountInformationRepository.save(accountInformation1);
//    }
//}
