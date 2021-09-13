package com.employees.services;

import com.employees.models.Employee;
import com.employees.models.Salary;
import com.employees.models.SalaryId;
import com.employees.repositories.EmployeeRepository;
import com.employees.repositories.SalaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

@Component
@AllArgsConstructor
public class IssueSalaries {

    private EmployeeRepository employeeRepository;

    private SalaryRepository salaryRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Scheduled(cron = "0 00 12 L * ?", zone = "Africa/Cairo")
    public void execute() {
        try {
            String dateAsString = dateFormat.format(new Date());
            Date date = null;
            date = dateFormat.parse(dateAsString);
            int month = date.getMonth();
            int year = date.getYear();

            //JAVA QUARTUS TEST DATA:


//            int month = 9;
//            int year = 2022;

//            Employee temp;
//            Employee employee = new Employee();
//            employee.setEmployeeId(1);
//            employee.setGender("male");
//            employee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/2001"));
//            employee.setNationalId(1234L);
//            employee.setFirstName("a");
//            employee.setLastName("b");
//            employee.setGrossSalary(12345.0);
//
//            Employee employee1 = new Employee();
//            employee1.setEmployeeId(2);
//            employee1.setGender("male");
//            employee1.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/2001"));
//            employee1.setNationalId(1235L);
//            employee1.setFirstName("b");
//            employee1.setLastName("b");
//            employee1.setGrossSalary(15000.0);
//            employee1.setRaise(1000.0);
//
//            Employee employee2 = new Employee();
//            employee2.setEmployeeId(3);
//            employee2.setBonus(500.0);
//            employee2.setGender("male");
//            employee2.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/2001"));
//            employee2.setNationalId(1236L);
//            employee2.setFirstName("c");
//            employee2.setLastName("c");
//            employee2.setGrossSalary(5000.0);
//
//            ArrayList<Employee> employees = new ArrayList<>();
//            employees.add(employee);
//            employees.add(employee1);
//            employees.add(employee2);


            Iterable<Employee> employee = employeeRepository.findAll();
            Iterator<Employee> employees = employee.iterator();
            Employee temp;
            while(employees.hasNext()) {
                temp = employees.next();
                System.out.println("employee before issue: " + "\n" + temp);
                SalaryId salaryId = new SalaryId();
                salaryId.setEmployee_id(temp.getEmployeeId());
                salaryId.setMonth(month);
                salaryId.setYear(year);
                Salary newSalary = new Salary();
                newSalary.setEmployee(temp);
                newSalary.setId(salaryId);
                newSalary.calculateNetSalary();
                salaryRepository.save(newSalary);
                System.out.println("salary for employee " + salaryId.getEmployee_id() + ": " + "\n" + newSalary);
                temp.setBonus(0.0);
                if (temp.getRaise() == null) {
                    temp.setRaise(0.0);
                }
                // System.out.println("g: " + temp.getGrossSalary() + ". r: " + temp.getRaise());
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
                System.out.println("employee after issue: " + "\n" + temp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

}
