package com.employees.repositories;


import com.employees.models.Department;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Integer>{
    @Transactional
    @Query("delete from Department x where x.departmentId = :id")
    @Modifying
    void deleteByID(Integer id);
}
