package com.clientsolve.poc;
import com.juliusbaer.itasia.crm.service.AccountService;
import com.juliusbaer.itasia.crm.service.AccountServiceImpl;

import mortgages.Applicant;
import mortgages.IncomeSource;
import mortgages.LoanApplication;

public class Main {

    public static void main(String[] args) {
        AccountService accountService = new AccountServiceImpl();
        
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setApproved(null);

        Applicant applicant = new Applicant();
        applicant.setAge(18);
        
        IncomeSource incomeSource = new IncomeSource();
        
        String explanation = accountService.validate(loanApplication, applicant, incomeSource);
        
        System.out.println(explanation);
    }
    
}
