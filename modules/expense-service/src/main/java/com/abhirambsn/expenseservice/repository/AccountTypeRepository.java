package com.abhirambsn.expenseservice.repository;

import com.abhirambsn.expenseservice.entity.account.AccountType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountTypeRepository extends MongoRepository<AccountType, String > {
}
