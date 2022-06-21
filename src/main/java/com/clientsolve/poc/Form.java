package com.clientsolve.poc;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.juliusbaer.itasia.crm.service.AccountService;
import com.juliusbaer.itasia.crm.service.AccountServiceImpl;

import mortgages.mortgages.Applicant;
import mortgages.mortgages.IncomeSource;
import mortgages.mortgages.LoanApplication;

@ManagedBean
@SessionScoped
public class Form {
    @ManagedProperty(value="#{applicant}")
    private Applicant applicant;
    @ManagedProperty(value="#{loanApplication}")
    private LoanApplication loanApplication;
    @ManagedProperty(value="#{incomeSource}")
    private IncomeSource incomeSource;
    
    private boolean hasIncome;
    private String status;
    private static final AccountService accountService = new AccountServiceImpl();
    
    public Applicant getApplicant() {
        return applicant;
    }
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
    public LoanApplication getLoanApplication() {
        return loanApplication;
    }
    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }
    public IncomeSource getIncomeSource() {
        return incomeSource;
    }
    public void setIncomeSource(IncomeSource incomeSource) {
        this.incomeSource = incomeSource;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public boolean isHasIncome() {
        return hasIncome;
    }
    public void setHasIncome(boolean hasIncome) {
        this.hasIncome = hasIncome;
    }
    
    public String submit() {
        Object explanation = null;
        
        if(hasIncome) {
            //explanation = accountService.validate(loanApplication, applicant, incomeSource);
        } else {
            //explanation = accountService.validate(loanApplication, applicant, null);
        }
        
        this.status = explanation != null ? String.valueOf(explanation) : "APPROVED";
        
        return status;
    }
}
