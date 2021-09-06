package com.employees.repositories;


import com.employees.models.AccountInformation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountInformationRepository extends CrudRepository<AccountInformation, String> {


    @Transactional
    @Query("delete from AccountInformation x where x.username = :user")
    @Modifying
    void deleteByUser(String user);
}
