package com.abhirambsn.expenseservice.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.abhirambsn.expenseservice.repository")
public class MultiTenantMongoAppConfig extends AbstractMongoClientConfiguration {
    @Autowired
    private MongoConfigProperties mongoConfigProperties;

    @Override
    protected String getDatabaseName() {
        return null;
    }

    @Override
    @Primary
    @Bean
    public @NonNull MongoDatabaseFactory mongoDbFactory() {
        String globalDb = mongoConfigProperties.getDatabaseName();
        return new MultiTenantMongoDbFactory(mongoClient(), globalDb);
    }

    @Override
    @Bean
    @Primary
    public @NonNull MongoTemplate mongoTemplate(@NonNull MongoDatabaseFactory mongoDbFactory, @NonNull MappingMongoConverter converter) {
        return new MongoTemplate(mongoDbFactory, converter);
    }


}
