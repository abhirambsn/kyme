package com.abhirambsn.expenseservice.dto.expense;

import com.abhirambsn.expenseservice.entity.account.Account;
import com.abhirambsn.expenseservice.entity.Category;
import com.abhirambsn.expenseservice.entity.expense.ExpenseType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ExpenseResponseDto {
    private String id;
    private String name;
    private String description;
    private Date createdDate;
    private Double amount;
    private String createdBy;

    private ExpenseType type;

    private Account fromAccount;
    private Account toAccount;

    private Category category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
