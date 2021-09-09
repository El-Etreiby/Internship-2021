package com.employees.services;

import com.employees.errorHandling.BadArgumentException;
import com.employees.models.AccountInformation;
import com.employees.repositories.AccountInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountInformationService {

    @Autowired
    private AccountInformationRepository accountInformationRepository;

}

