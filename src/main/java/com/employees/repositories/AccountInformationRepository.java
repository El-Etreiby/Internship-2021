package com.employees.repositories;


import com.employees.models.AccountInformation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountInformationRepository extends CrudRepository<AccountInformation, String> {

}
