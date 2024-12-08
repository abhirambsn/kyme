package com.abhirambsn.expenseservice.dto.expense;

import com.abhirambsn.expenseservice.entity.expense.ExpenseType;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateExpenseDto {
    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private Date createdDate;

    @NonNull
    private ExpenseType type;

    @NonNull
    private Double amount;

    @NonNull
    private String fromAccount;

    private String toAccount;

    @NonNull
    private String createdBy;

    @NonNull
    private String category;

}
