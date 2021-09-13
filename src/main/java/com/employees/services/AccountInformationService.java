package com.employees.services;

import com.employees.repositories.AccountInformationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountInformationService {
    private AccountInformationRepository accountInformationRepository;

}

