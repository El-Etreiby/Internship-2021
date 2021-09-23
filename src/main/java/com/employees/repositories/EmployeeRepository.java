package com.employees.repositories;

import com.employees.DTOs.EmployeeDto;
import com.employees.models.Employee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    @Transactional
    @Query("delete from Employee x where x.employeeId = :id")
    @Modifying
    void deleteByEmployeeId(Integer id);

    @Transactional
    @Query(value = "with recursive cte (employee_id, first_name, last_name, manager_id, dob, gender, graduation_date, gross_salary, department_id, team_id, expertise, days_off_taken, degree, years_of_experience, national_id, bonus, raise, hire_date) as (\n" +
            "    select     e1.employee_id, e1.first_name, e1.last_name, e1.manager_id, e1.dob, e1.gender, e1.graduation_date, e1.gross_salary, e1.department_id, e1.team_id, e1.expertise, e1.days_off_taken, e1.degree, e1.years_of_experience, e1.national_id, e1.bonus, e1.raise, e1.hire_date\n" +
            "    from       employee e1\n" +
            "    where      manager_id = :managerIdParam\n" +
            "    union all\n" +
            "    select     e.employee_id, e.first_name, e.last_name, e.manager_id, e.dob, e.gender, e.graduation_date, e.gross_salary, e.department_id, e.team_id, e.expertise, e.days_off_taken, e.degree, e.years_of_experience, e.national_id, e.bonus, e.raise, e.hire_date\n" +
            "    from       employee e\n" +
            "                   inner join cte\n" +
            "                              on e.manager_id = cte.employee_id\n" +
            ")\n" +
            "select * from cte;", nativeQuery = true)
    List<Employee> getAllManagedEmployees(int managerIdParam);


    @Transactional
    @Query(value="select * from employee e where e.team_id= :teamId", nativeQuery = true)
    ArrayList<Employee> getEmployeesInTeam(int teamId);
}
//e.employeeId, e.firstName, e.lastName, e.dob, e.gender, e.graduationDate, e.employeeTeam.teamId, e.manager.employeeId, e.department.departmentId, e.grossSalary, e.bonus, e.raise, e.degree, e.expertise, e.yearsOfExperience, e.daysOffTaken, e.hireDate, e.nationalId