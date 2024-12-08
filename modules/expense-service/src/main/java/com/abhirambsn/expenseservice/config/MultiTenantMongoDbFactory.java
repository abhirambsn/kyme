package com.abhirambsn.expenseservice.config;

import com.abhirambsn.expenseservice.tenant.TenantContext;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;


public class MultiTenantMongoDbFactory extends SimpleMongoClientDatabaseFactory {
    @Autowired
    private TenantContext tenantContext;
    private final String globalDb;

    public MultiTenantMongoDbFactory(MongoClient mongoClient, String globalDb) {
        super(mongoClient, globalDb);
        this.globalDb = globalDb;
    }

    @Override
    public MongoDatabase getMongoDatabase() {
        return getMongoClient().getDatabase(getTenantDatabase());
    }

    protected String getTenantDatabase() {
        String tenantId = tenantContext.getTenantId();
        if (tenantId != null) {
            return tenantId;
        } else {
         return globalDb;
        }

    }


}
