package com.abhirambsn.expenseservice.repository;

import com.abhirambsn.expenseservice.entity.NetAmount;
import com.abhirambsn.expenseservice.entity.expense.Expense;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    @Query(value = "{}", sort = "{ '?2': 1 }")
    List<Expense> findAllWithFilter(int top, int skip, String orderBy, Pageable pageable);

    @Aggregation(
            pipeline = {
                    "{ $match: { $or: [ { 'toAccount._id': ObjectId(?0) }, { 'fromAccount._id': ObjectId(?0) } ] } }",
                    "{ $group: { " +
                            "     _id: null, " +
                            "     totalToAccount: { $sum: { $cond: [ { $eq: [ '$toAccount._id', ObjectId(?0) ] }, '$amount', 0 ] } }, " +
                            "     totalFromAccount: { $sum: { $cond: [ { $eq: [ '$fromAccount._id', ObjectId(?0) ] }, '$amount', 0 ] } } " +
                            "} }",
                    "{ $project: { " +
                            "     _id: 0, " +
                            "     netAmount: { $subtract: [ '$totalToAccount', '$totalFromAccount' ] } " +
                            "} }"
            }
    )
    NetAmount getTotalExpenseByAccount(String accountId);

    List<Expense> findAllByCategoryId(String categoryId);
    List<Expense> findExpenseByFromAccountIdOrToAccountId(String fromAccount_id, String toAccount_id);
}
