package com.abhirambsn.expenseservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MongoConfigProperties {
    @Value("${spring.data.mongodb.database:global}")
    private String databaseName;

    private String username = "root";
    private String password = "example";
    private String host = "localhost";
    private int port = 27017;
}
