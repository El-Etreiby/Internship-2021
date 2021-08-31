package com.employees.repositories;

import com.employees.models.Employee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    @Transactional
    @Query("delete from Employee x where x.employeeId = :id")
    @Modifying
    void deleteByEmployeeId(Integer id);

//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.netSalary = :netSalary where e.employeeId = :parseInt")
//    void updateEmployeeNetSalary(int parseInt, Double netSalary);

    @Transactional
    @Query(value = "with recursive cte (employee_id, first_name, last_name, manager_id, dob, gender, graduation_date, gross_salary, department_id, team_id, expertise, days_off_taken, degree, years_of_experience, national_id) as (\n" +
            "    select     e1.employee_id, e1.first_name, e1.last_name, e1.manager_id, e1.dob, e1.gender, e1.graduation_date, e1.gross_salary, e1.department_id, e1.team_id, e1.expertise, e1.days_off_taken, e1.degree, e1.national_id, e1.years_of_experience\n" +
            "    from       employee e1\n" +
            "    where      manager_id = :managerIdParam\n" +
            "    union all\n" +
            "    select     e.employee_id, e.first_name, e.last_name, e.manager_id, e.dob, e.gender, e.graduation_date, e.gross_salary, e.days_off_taken, e.degree, e.national_id, e.years_of_experience, e.department_id, e.team_id, e.expertise\n" +
            "    from       employee e\n" +
            "                   inner join cte\n" +
            "                              on e.manager_id = cte.employee_id\n" +
            ")\n" +
            "select * from cte;", nativeQuery = true)
    List<Employee> getAllManagedEmployees(int managerIdParam);
}
