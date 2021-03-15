package com.juliusbaer.itasia.crm.service;

import java.util.logging.Logger;

import org.kie.api.runtime.rule.FactHandle;

import com.juliusbaer.itasia.crm.migration.CacheUtil;
import com.juliusbaer.itasia.crm.migration.KieServicesAdapter;
import com.juliusbaer.itasia.crm.reconstruct.BRMSKnowledgeBaseEnum;
import com.juliusbaer.itasia.crm.reconstruct.CRMConstant;
import com.juliusbaer.itasia.crm.reconstruct.CRMException;

import mortgages.Applicant;
import mortgages.IncomeSource;
import mortgages.LoanApplication;

public class AccountServiceImpl implements AccountService {
    private static final Logger aLog = Logger.getLogger("ATL");

    public String validate(LoanApplication loanApplication, Applicant applicant, IncomeSource incomeSource) {
        aLog.info("AccountServiceImpl - underage - Start");
        long currentMilis = System.currentTimeMillis();
        String explanation = null;
        //KnowledgeBase kBase = null;
        KieServicesAdapter session = null;
        Object appObj = null;
        Object loanAppObj = null;
        Object incomeSrcObj = null;

        try {
            appObj = createForInputToRules(applicant, CRMConstant.ACCOUNT_RESIDENCE);
            loanAppObj = createForInputToRules(loanApplication, CRMConstant.ACCOUNT_RESIDENCE);
            if (incomeSource != null) {
                incomeSrcObj = createForInputToRules(incomeSource, CRMConstant.ACCOUNT_RESIDENCE);
            }
            
            aLog.info("validate - pre appObj: " + appObj);

            session = CacheUtil.getBRMSKieServicesCache().getKieServices(BRMSKnowledgeBaseEnum.KBASE_ACC_VALIDATION);

            session.insert(appObj);
            session.insert(loanAppObj);
            if (incomeSrcObj != null) {
                session.insert(incomeSrcObj);
            }

            session.fireAllRules();

            FactHandle f1 = session.getFactHandle(appObj);

            explanation = ((LoanApplication) session.getObject(loanAppObj)).getExplanation();

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

        aLog.info("validate - post appObj: " + appObj);

        aLog.info("AccountServiceImpl - validate - Time taken: "
                + (System.currentTimeMillis() - currentMilis) + " ms");
        aLog.info("AccountServiceImpl - validate - End " + explanation);

        return explanation;

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
