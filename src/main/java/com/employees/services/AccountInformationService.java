package com.employees.services;

import com.employees.errorHandling.IncorrectPasswordException;
import com.employees.errorHandling.InvalidUsernameException;
import com.employees.models.AccountInformation;
import com.employees.repositories.AccountInformationRepository;
import com.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountInformationService {

    @Autowired
    private AccountInformationRepository accountInformationRepository;

    public void addAccount(AccountInformation accountInformation){
        accountInformationRepository.save(accountInformation);
    }

    public void removeAccount(String username) throws InvalidUsernameException {
        Optional<AccountInformation> accountInfo = accountInformationRepository.findById(username);
        if(!accountInfo.isPresent()){
            throw new InvalidUsernameException("This username does not exist!");
        }
        accountInformationRepository.deleteById(username);
    }

    public Integer verifyCredentials(String username, String password) throws InvalidUsernameException, IncorrectPasswordException {
        Optional<AccountInformation> accountInfo = accountInformationRepository.findById(username);
        if(!accountInfo.isPresent()){
            throw new InvalidUsernameException("This username does not exist!");
        }
        if(!accountInfo.get().getPassword().equals(password)){
            throw new IncorrectPasswordException("Incorrect password for this username");
        }
        return accountInfo.get().getEmployee().getEmployeeId();
    }
}

