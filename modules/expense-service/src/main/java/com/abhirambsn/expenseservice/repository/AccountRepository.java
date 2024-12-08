package com.abhirambsn.expenseservice.repository;

import com.abhirambsn.expenseservice.entity.account.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    @Query(value = "{}", sort = "{ '?2': 1 }")
    List<Account> findAllWithFilter(int top, int skip, String orderBy, Pageable pageable);
}
