package com.juliusbaer.itasia.crm.migration;

import com.juliusbaer.itasia.crm.reconstruct.BRMSKnowledgeBaseEnum;
import com.juliusbaer.itasia.crm.reconstruct.ICache;

public interface IBRMSKieServicesCache extends ICache {
    
    public void initialize();
    
    public KieServicesAdapter getKieServices(BRMSKnowledgeBaseEnum brmsKnowBase);
    
}
