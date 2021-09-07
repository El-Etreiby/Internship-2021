package com.employees.repositories;


import com.employees.models.AccountInformation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountInformationRepository extends CrudRepository<AccountInformation, Integer> {


    @Transactional
    @Query("delete from AccountInformation x where x.username = :user")
    @Modifying
    void deleteByUsername(String user);

    @Transactional
    @Query("select x from AccountInformation x where x.employee.employeeId = :employeeId")
    Optional<AccountInformation> findByEmployeeId(int employeeId);

    @Transactional
    @Query("select x from AccountInformation x where x.username = :username")
    Optional<AccountInformation> findByUsername(String username);

}
