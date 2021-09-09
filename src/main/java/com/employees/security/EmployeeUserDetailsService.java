package com.employees.security;

import com.employees.models.AccountInformation;
import com.employees.repositories.AccountInformationRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountInformationRepository accountInformationRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String s) {

        Optional<AccountInformation> accountInformation = accountInformationRepository.findByUsername(s);
        if(!accountInformation.isPresent()) {
            return null;
        }
        EmployeeUserDetails employeeDetails = new EmployeeUserDetails(accountInformation.get());
        return employeeDetails;
    }
}
