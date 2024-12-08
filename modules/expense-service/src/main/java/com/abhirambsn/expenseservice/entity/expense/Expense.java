package com.abhirambsn.expenseservice.entity.expense;

import com.abhirambsn.expenseservice.entity.account.Account;
import com.abhirambsn.expenseservice.entity.BaseEntity;
import com.abhirambsn.expenseservice.entity.Category;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "expenses")
public class Expense extends BaseEntity {
    @Id
    private String id;

    private String name;
    private String description;
    private Date createdDate;
    private String createdBy;

    private Double amount;

    private ExpenseType type;

    @DBRef
    private Account fromAccount;

    @DBRef
    private Account toAccount;

    @DBRef
    private Category category;
}
