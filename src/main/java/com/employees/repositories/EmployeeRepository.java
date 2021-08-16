package com.employees.repositories;

import com.employees.models.Employee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


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
}
