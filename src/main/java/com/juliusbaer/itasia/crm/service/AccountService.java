package com.juliusbaer.itasia.crm.service;

import mortgages.Applicant;
import mortgages.IncomeSource;
import mortgages.LoanApplication;

public interface AccountService {

    public String validate(LoanApplication loanApplication, Applicant applicant, IncomeSource incomeSource);
    
}
