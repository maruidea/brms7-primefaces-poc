package com.clientsolve.poc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;

import com.juliusbaer.itasia.crm.service.AccountService;
import com.juliusbaer.itasia.crm.service.AccountServiceImpl;

import mortgages.mortgages.Applicant;
import mortgages.mortgages.CreditFieldInfo;
import mortgages.mortgages.CustomApplicant;
import mortgages.mortgages.IncomeSource;
import mortgages.mortgages.LoanApplication;

public class Main3 {

    public static void main(String[] args) {
        String serverUrl = "http://vm1.atiila.id:8181/kie-server/services/rest/servers";
        String username = "pamAdmin";
        String password = "redhatpam1!";
        
        // Register used java bean classes
        Set<Class<?>> extraClassList = new HashSet<Class<?>>();
        extraClassList.add(CreditFieldInfo.class);
        
        KieServicesConfiguration jaxbConfig = KieServicesFactory.newRestConfiguration(serverUrl, username, password);
        jaxbConfig.setMarshallingFormat(MarshallingFormat.JAXB);
        
        KieServicesClient jaxbKieServicesClient = KieServicesFactory.newKieServicesClient(jaxbConfig);
        jaxbConfig.addExtraClasses(extraClassList);
    }
    
}
