package com.juliusbaer.itasia.crm.migration;

import com.juliusbaer.itasia.crm.reconstruct.BRMSKnowledgeBaseEnum;

public interface IBRMSKieServicesCache {
    
    public void initialize();
    
    public KieServicesAdapter getKieServices(BRMSKnowledgeBaseEnum brmsKnowBase);
    
}
