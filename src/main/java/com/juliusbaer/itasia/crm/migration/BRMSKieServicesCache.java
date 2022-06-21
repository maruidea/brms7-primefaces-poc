package com.juliusbaer.itasia.crm.migration;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;

import com.juliusbaer.itasia.crm.reconstruct.BRMSKnowledgeBaseEnum;
import com.juliusbaer.itasia.crm.reconstruct.CRMException;
import com.juliusbaer.itasia.crm.reconstruct.MonitoringConstants;

import mortgages.mortgages.Applicant;
import mortgages.mortgages.IncomeSource;
import mortgages.mortgages.LoanApplication;

public class BRMSKieServicesCache implements IBRMSKieServicesCache {
    private static final Logger aLog = Logger.getLogger("ATL");
    private static final Logger eLog = Logger.getLogger("EXL");

    private static Map<String, KieServicesAdapter> cache = null;
    private static BRMSKieServicesCache singleton = null;

    /**
     * Private constructor to prevent creating instances for this class. Use
     * <code>getInstance</code> instead.
     */
    protected BRMSKieServicesCache() {
        cache = new HashMap<String, KieServicesAdapter>();
    }

    /**
     * Loads all knowledge base configuration from BRMS.
     */
    public void initialize() {
        aLog.info("BRMSKieServiceCache - initialize - Start");
        long currentMilis = System.currentTimeMillis();
        
        Map<String, KieServicesAdapter> tmpCache = new HashMap<String, KieServicesAdapter>();
        String envInstance = System.getProperty("env-instance") != null ? System.getProperty("env-instance") : System.getenv("ENV_INSTANCE");
        tmpCache.put(
          BRMSKnowledgeBaseEnum.MORTGAGES.toString(),
          readKieServices("mortgages-properties-"+ envInstance +".properties"));
        tmpCache.put(
                BRMSKnowledgeBaseEnum.MORTGAGE_PROCESS.toString(),
                readKieServices("mortgage-process-properties-"+ envInstance +".properties"));
      
        cache.putAll(tmpCache);
          
        aLog.info("BRMSKieServiceCache - initialize - Time taken to initialize cache: "
                + (System.currentTimeMillis() - currentMilis) + " ms");
        aLog.info("BRMSKieServiceCache - initialize - end");
    }
    
    public KieServicesAdapter getKieServices(BRMSKnowledgeBaseEnum brmsKnowBase) {
        return cache.get(brmsKnowBase.toString());
    }

    private KieServicesAdapter readKieServices(String filename) {
        aLog.info("BRMSKieServicesCache - readKieServices - Start");
        KieServicesAdapter kieServicesAdapter = null;
        
        try {
            String envInstance = System.getProperty("env-instance") != null ? System.getProperty("env-instance") : System.getenv("ENV_INSTANCE");
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(envInstance + File.separatorChar + filename));
            
            String serverUrl = properties.getProperty("server");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            
            // Register used java bean classes
            Set<Class<?>> extraClassList = new HashSet<Class<?>>();
            extraClassList.add(LoanApplication.class);
            extraClassList.add(Applicant.class);
            extraClassList.add(IncomeSource.class);
            extraClassList.add(com.myspace.mortgage_app.Application.class);
            extraClassList.add(com.myspace.mortgage_app.Applicant.class);
            extraClassList.add(com.myspace.mortgage_app.Property.class);
            extraClassList.add(com.myspace.mortgage_app.ValidationErrorDO.class);
            
            KieServicesConfiguration jaxbConfig = KieServicesFactory.newRestConfiguration(serverUrl, username, password);
            jaxbConfig.setMarshallingFormat(MarshallingFormat.JAXB);

            KieServicesConfiguration jsonConfig = KieServicesFactory.newRestConfiguration(serverUrl, username, password);
            jsonConfig.setMarshallingFormat(MarshallingFormat.JSON);
            jsonConfig.addExtraClasses(extraClassList);

            KieServicesConfiguration xStreamConfig = KieServicesFactory.newRestConfiguration(serverUrl, username, password);
            xStreamConfig.setMarshallingFormat(MarshallingFormat.XSTREAM);
            xStreamConfig.addExtraClasses(extraClassList);
            // Create servicesClient
            KieServicesClient jaxbKieServicesClient = KieServicesFactory.newKieServicesClient(jaxbConfig);
            KieServicesClient jsonKieServicesClient = KieServicesFactory.newKieServicesClient(jsonConfig);
            KieServicesClient xStreamKieServicesClient = KieServicesFactory.newKieServicesClient(xStreamConfig);

            kieServicesAdapter = new KieServicesAdapter(jaxbKieServicesClient, jsonKieServicesClient, xStreamKieServicesClient, properties.getProperty("container"));
        } catch (Exception e) {
            eLog.log(Level.SEVERE, MonitoringConstants.ERR_CRM_000009 + " Exception occurred while creating BRMS Kie Services: " + e);
            throw new CRMException("Exception occurred while creating BRMS BRMS Kie Services:e ", e);        
        }
        return kieServicesAdapter;
    }

    public static BRMSKieServicesCache getInstance() {
        if(singleton == null) {
            singleton = new BRMSKieServicesCache();
        }
        return singleton;
    }
}
