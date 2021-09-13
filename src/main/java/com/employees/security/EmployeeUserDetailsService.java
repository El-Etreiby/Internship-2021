package com.employees.security;

import com.employees.models.AccountInformation;
import com.employees.repositories.AccountInformationRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeUserDetailsService implements UserDetailsService {

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
