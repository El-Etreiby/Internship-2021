package com.employees.repositories;

import com.employees.models.Employee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    @Modifying
    @Query("update Employee e set e.employeeTeam = :teamId where e.employeeId = :employeeId")
    public void addEmployeeToTeam(Integer employeeId, Integer teamId);
}
