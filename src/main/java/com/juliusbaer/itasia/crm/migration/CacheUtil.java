package com.juliusbaer.itasia.crm.migration;

public class CacheUtil {
    public static BRMSKieServicesCache brmsKieServicesCache = null;

    public static BRMSKieServicesCache getBRMSKieServicesCache() {
        if (brmsKieServicesCache == null) {
            brmsKieServicesCache = BRMSKieServicesCache.getInstance();
            brmsKieServicesCache.initialize();
        }
        
        return brmsKieServicesCache;
    }

}
