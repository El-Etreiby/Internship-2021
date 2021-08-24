package com.employees.repositories;

import com.employees.models.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.employeeTeam.teamId = :teamId where e.employeeId = :employeeId")
//    public void addEmployeeToTeam(Integer employeeId, Integer teamId);
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.department.departmentId = :departmentId where e.employeeId = :employeeId")
//    void addEmployeeToDepartment(Integer employeeId, Integer departmentId);
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.manager.employeeId = :managerId where e.employeeId = :employeeId")
//    void addManagerToEmployee(Integer employeeId, Integer managerId);
//
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.employeeName = :employeeName where e.employeeId = :parseInt")
//    void updateEmployeeName(int parseInt, String employeeName);
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.department.departmentId = :department where e.employeeId = :parseInt")
//    void updateEmployeeDepartment(int parseInt, Integer department);
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.employeeTeam.teamId = :employeeTeam where e.employeeId = :parseInt")
//    void updateEmployeeTeam(int parseInt, Integer employeeTeam);
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.dob = :dob where e.employeeId = :parseInt")
//    void updateEmployeeDob(int parseInt, Date dob);
//
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.gender = :gender where e.employeeId = :parseInt")
//    void updateEmployeeGender(int parseInt, char gender);
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.graduationDate = :graduationDate where e.employeeId = :parseInt")
//    void updateEmployeeGraduationDate(int parseInt, Date graduationDate);
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.grossSalary = :grossSalary where e.employeeId = :parseInt")
//    void updateEmployeeGrossSalary(int parseInt, Double grossSalary);
//
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.manager.employeeId = :manager where e.employeeId = :parseInt")
//    void updateEmployeeManager(int parseInt, Integer manager);
//
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.netSalary = :netSalary where e.employeeId = :parseInt")
//    void updateEmployeeNetSalary(int parseInt, Double netSalary);

    @Transactional
    @Query(value="with recursive cte (employee_id, employee_name, manager_id, dob, gender, graduation_date, gross_salary, net_salary, department_id, team_id, expertise) as (\n" +
            "    select     employee.employee_id, employee.employee_name, employee.manager_id, employee.dob, employee.gender, employee.graduation_date, employee.gross_salary, employee.net_salary, employee.department_id, employee.team_id, employee.expertise\n" +
            "    from       employee\n" +
            "    where      manager_id = :managerIdParam\n" +
            "    union all\n" +
            "    select     e.employee_id, e.employee_name, e.manager_id, e.dob, e.gender, e.graduation_date, e.gross_salary, e.net_salary, e.department_id, e.team_id, e.expertise\n" +
            "    from       employee e\n" +
            "                   inner join cte\n" +
            "                              on e.manager_id = cte.employee_id\n" +
            ")\n" +
            "select * from cte;",nativeQuery = true)
    List<Employee> getAllManagedEmployees(int managerIdParam);

//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.manager = null where e.employeeId = :employeeId")
//    void removeEmployeesManager(int employeeId);
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.employeeTeam = null where e.employeeId = :employeeId")
//    void removeEmployeesTeam(int employeeId);
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.department = null where e.employeeId = :employeeId")
//    void removeEmployeesDepartment(int employeeId);
//
//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.expertise = :expertise where e.employeeId = :parseInt")
//    void updateEmployeeExpertise(int parseInt, String expertise);
}
