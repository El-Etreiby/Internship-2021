package com.employees.repositories;

import com.employees.models.Salary;
import com.employees.models.SalaryId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SalaryRepository extends CrudRepository<Salary, SalaryId> {

    @Transactional
    @Query(value = "select * from Salary s where s.employee_id = :id", nativeQuery=true)
    List<Salary> getByEmployeeId(Integer id);
}
