package com.clientsolve.poc;
import java.util.ArrayList;

import com.juliusbaer.itasia.crm.service.AccountService;
import com.juliusbaer.itasia.crm.service.AccountServiceImpl;

import mortgages.mortgages.CustomApplicant;
import mortgages.mortgages.IncomeSource;
import mortgages.mortgages.LoanApplication;

public class Main {

    public static void main(String[] args) {
        AccountService accountService = new AccountServiceImpl();
        
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setApproved(null);

        CustomApplicant applicant = new CustomApplicant();
        applicant.setAge(18);
        //applicant.setLoans(new ArrayList<LoanApplication>());
        
        IncomeSource incomeSource = new IncomeSource();
        
        String explanation = accountService.validate(loanApplication, applicant, incomeSource);
        
        System.out.println(explanation);
    }
    
}
