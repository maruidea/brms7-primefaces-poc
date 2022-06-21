package com.juliusbaer.itasia.crm.service;

import java.util.logging.Logger;

import org.kie.api.runtime.rule.FactHandle;

import com.juliusbaer.itasia.crm.migration.CacheUtil;
import com.juliusbaer.itasia.crm.migration.KieServicesAdapter;
import com.juliusbaer.itasia.crm.reconstruct.BRMSKnowledgeBaseEnum;
import com.juliusbaer.itasia.crm.reconstruct.CRMConstant;
import com.juliusbaer.itasia.crm.reconstruct.CRMException;
import com.myspace.mortgage_app.Application;

import mortgages.mortgages.Applicant;
import mortgages.mortgages.CustomApplicant;
import mortgages.mortgages.IncomeSource;
import mortgages.mortgages.LoanApplication;

public class AccountServiceImpl implements AccountService {
    private static final Logger aLog = Logger.getLogger("ATL");

    public String validate(LoanApplication loanApplication, CustomApplicant applicant, IncomeSource incomeSource) {
        aLog.info("AccountServiceImpl - underage - Start");
        long currentMilis = System.currentTimeMillis();
        String explanation = null;
        //KnowledgeBase kBase = null;
        KieServicesAdapter session = null;
        Object loanAppObj = null;
        Object incomeSrcObj = null;

        try {
            loanAppObj = createForInputToRules(loanApplication, CRMConstant.ACCOUNT_RESIDENCE);
            if (incomeSource != null) {
                incomeSrcObj = createForInputToRules(incomeSource, CRMConstant.ACCOUNT_RESIDENCE);
            }
            
            session = CacheUtil.getBRMSKieServicesCache().getKieServices(BRMSKnowledgeBaseEnum.MORTGAGES);
            
            session.insertAs(applicant, Applicant.class);
            session.insert(loanAppObj);
            if (incomeSrcObj != null) {
                session.insert(incomeSrcObj);
            }
            aLog.info("FIRE_ALL_RULES");
            session.fireAllRules();
            aLog.info("GET_OBJECTS");
            session.getObjects();
            aLog.info("GET_FACT_HANDLES");
            session.getFactHandles();
            
            FactHandle f1 = session.getFactHandle(loanAppObj);
            explanation = ((LoanApplication) session.getObject(f1)).getExplanation();

            session.retract(f1);

            for (FactHandle fact : session.getFactHandles()) {
                session.retract(fact);
            }
            
        } catch (Exception e) {
            aLog.severe("Exception occurred while validating applicant with BRMS rules: " + e);
            throw new CRMException("Exception occurred while validating applicant with BRMS rules: ", e);
        } finally {
            if (session != null) {
                session.dispose();
            }
        }

        aLog.info("AccountServiceImpl - validate - Time taken: "
                + (System.currentTimeMillis() - currentMilis) + " ms");
        aLog.info("AccountServiceImpl - validate - End " + explanation);

        return explanation;

    }

    public Application testProcess(Application application) {
        KieServicesAdapter session = null;
        Application result = null;
        
        try {
            session = CacheUtil.getBRMSKieServicesCache().getKieServices(BRMSKnowledgeBaseEnum.MORTGAGE_PROCESS);

            session.insert(application);
            session.startProcess("Mortgage_Process.MortgageApprovalProcess");
            session.fireAllRules();

            result = (Application) session.getObject(session.getFactHandle(application));
            
            for (FactHandle fact : session.getFactHandles()) {
                session.retract(fact);
            }
            
        } catch (Exception e) {
            throw new CRMException("Exception occurred while validating applicant with BRMS rules: ", e);
        } finally {
            if (session != null) {
                session.dispose();
            }
        }

        return result;
    }

    private Object createForInputToRules(IncomeSource incomeSource, String accountResidence)
            throws InstantiationException, IllegalAccessException {
        return incomeSource;
    }

    private Object createForInputToRules(LoanApplication loanApplication, String accountResidence) 
            throws InstantiationException, IllegalAccessException {
        return loanApplication;
    }

    private Object createForInputToRules(Applicant applicant, String accountResidence) 
            throws InstantiationException, IllegalAccessException {
        return applicant;
    }

}
