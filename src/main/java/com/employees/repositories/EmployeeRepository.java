package com.employees.repositories;

import com.employees.models.Department;
import com.employees.models.Employee;
import com.employees.models.Team;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    @Transactional
    @Modifying
    @Query("update Employee e set e.employeeTeam.teamId = :teamId where e.employeeId = :employeeId")
    public void addEmployeeToTeam(Integer employeeId, Integer teamId);

    @Transactional
    @Modifying
    @Query("update Employee e set e.department.departmentId = :departmentId where e.employeeId = :employeeId")
    void addEmployeeToDepartment(Integer employeeId, Integer departmentId);
    @Transactional
    @Modifying
    @Query("update Employee e set e.manager.employeeId = :managerId where e.employeeId = :employeeId")
    void addManagerToEmployee(Integer employeeId, Integer managerId);


    @Transactional
    @Modifying
    @Query("update Employee e set e.employeeName = :employeeName where e.employeeId = :parseInt")
    void updateEmployeeName(int parseInt, String employeeName);

//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.department = :department where e.employeeId = :parseInt")
//    void updateEmployeeDepartment(int parseInt, Department department);

//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.employeeTeam = :employeeTeam where e.employeeId = :parseInt")
//    void updateEmployeeTeam(int parseInt, Team employeeTeam);

    @Transactional
    @Modifying
    @Query("update Employee e set e.dob = :dob where e.employeeId = :parseInt")
    void updateEmployeeDob(int parseInt, Date dob);


    @Transactional
    @Modifying
    @Query("update Employee e set e.gender = :gender where e.employeeId = :parseInt")
    void updateEmployeeGender(int parseInt, char gender);

    @Transactional
    @Modifying
    @Query("update Employee e set e.graduationDate = :graduationDate where e.employeeId = :parseInt")
    void updateEmployeeGraduationDate(int parseInt, Date graduationDate);

    @Transactional
    @Modifying
    @Query("update Employee e set e.grossSalary = :grossSalary where e.employeeId = :parseInt")
    void updateEmployeeGrossSalary(int parseInt, Double grossSalary);


    @Transactional
    @Modifying
    @Query("update Employee e set e.manager = :manager where e.employeeId = :parseInt")
    void updateEmployeeManager(int parseInt, Employee manager);


//    @Transactional
//    @Modifying
//    @Query("update Employee e set e.managedEmployees = :managedEmployees where e.employeeId = :parseInt")
//    void updateEmployeeManagedEmployees(int parseInt, List<Employee> managedEmployees);

    @Transactional
    @Modifying
    @Query("update Employee e set e.netSalary = :netSalary where e.employeeId = :parseInt")

    void updateEmployeeNetSalary(int parseInt, Double netSalary);

    @Transactional
    @Modifying
    @Query("update Employee e set e.manager = null where e.employeeId = :employeeId")

    void removeEmployeesManager(int employeeId);
}
