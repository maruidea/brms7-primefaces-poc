package com.juliusbaer.itasia.crm.migration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;

import com.juliusbaer.itasia.crm.reconstruct.BRMSKnowledgeBaseEnum;

import mortgages.Applicant;
import mortgages.IncomeSource;
import mortgages.LoanApplication;

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

        // Temporary implementation: store the connection info in variables
        String serverUrl = "http://vm1.atiila.id:8181/kie-server/services/rest/server";
        String username = "pamAdmin";
        String password = "redhatpam1!";
        
        // Register used java bean classes
        Set<Class<?>> extraClassList = new HashSet<Class<?>>();
        extraClassList.add(LoanApplication.class);
        extraClassList.add(Applicant.class);
        extraClassList.add(IncomeSource.class);

        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(serverUrl, username, password);
        config.setMarshallingFormat(MarshallingFormat.JAXB);
        config.addExtraClasses(extraClassList);

        // Create servicesClient
        KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(config);

        Map<String, KieServicesAdapter> tmpCache = new HashMap<String, KieServicesAdapter>();
        tmpCache.put(
          BRMSKnowledgeBaseEnum.KBASE_ACC_VALIDATION.toString(),
          readKieServices(kieServicesClient, "mortgages_1.0.0-SNAPSHOT"));
      
        cache.putAll(tmpCache);
          
        aLog.info("BRMSKieServiceCache - initialize - Time taken to initialize cache: "
                + (System.currentTimeMillis() - currentMilis) + " ms");
        aLog.info("BRMSKieServiceCache - initialize - end");
    }
    
    public KieServicesAdapter getKieServices(BRMSKnowledgeBaseEnum brmsKnowBase) {
        return cache.get(brmsKnowBase.toString());
    }

    private KieServicesAdapter readKieServices(KieServicesClient kieServicesClient, String containerId) {
        KieServicesAdapter kieServicesAdapter = new KieServicesAdapter(kieServicesClient, containerId);
        return kieServicesAdapter;
        
        /*aLog.info("BRMSKnowledgeBaseCache - readKnowledgeBase - Start");
        KnowledgeBase kBase = null;

        try {
            KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            Resource changeset = ResourceFactory.newFileResource(new File(getClass().getClassLoader().getResource(fileName).toURI()));
            kBuilder.add(changeset, ResourceType.CHANGE_SET);
            KnowledgeBuilderErrors errors = kBuilder.getErrors();
            if (CollectionsUtil.isNotBlank(errors)) {
                for (KnowledgeBuilderError error : errors) {
                    eLog.severe(MonitoringConstants.ERR_CRM_000009
                            + " BRMSKnowledgeBaseCache - readKnowledgeBase - Error occurred in reading resource: "
                            + error + "with error message as: " + error.getMessage());
                }
                throw new CRMException("Unable to read from Rule resources");
            }
            kBase = KnowledgeBaseFactory.newKnowledgeBase();
            kBase.addKnowledgePackages(kBuilder.getKnowledgePackages());
            Collection<KnowledgePackage> kPackageList = kBase.getKnowledgePackages();
            if (CollectionsUtil.isNotBlank(kPackageList)) {
                for (KnowledgePackage kPack : kPackageList) {
                    aLog.info("BRMSKnowledgeBaseCache - readKnowledgeBase - Knowledge packages: " + kPack.getName());
                    Collection<Rule> ruleList = kPack.getRules();
                    for (Rule rule : ruleList) {
                        aLog.info("BRMSKnowledgeBaseCache - readKnowledgeBase - Rules: " + rule.getName());
                    }
                }
            } else {
                aLog.info("BRMSKnowledgeBaseCache - readKnowledgeBase - No Knowledge package read");
            }
        } catch (Exception e) {
            e.printStackTrace();
            eLog.severe(MonitoringConstants.ERR_CRM_000009 + " Exception occurred while creating BRMS knowledge base: "
                    + e);
            throw new CRMException("Exception occurred while creating BRMS knowledge base:e ", e);
        }
        return kBase;*/
    }

    public static BRMSKieServicesCache getInstance() {
        if(singleton == null) {
            singleton = new BRMSKieServicesCache();
        }
        return singleton;
    }
}
