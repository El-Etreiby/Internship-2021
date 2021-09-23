package com.employees.repositories;

import com.employees.DTOs.EmployeeDto;
import com.employees.models.Team;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
public interface TeamRepository extends CrudRepository<Team,Integer>{

    @Transactional
    @Query("delete from Team x where x.teamId = :id")
    @Modifying
    void deleteByID(Integer id);

}
