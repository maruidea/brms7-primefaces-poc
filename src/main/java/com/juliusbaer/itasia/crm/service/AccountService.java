package com.juliusbaer.itasia.crm.service;

import mortgages.mortgages.CustomApplicant;
import mortgages.mortgages.IncomeSource;
import mortgages.mortgages.LoanApplication;

public interface AccountService {

    public String validate(LoanApplication loanApplication, CustomApplicant applicant, IncomeSource incomeSource);
    
}
