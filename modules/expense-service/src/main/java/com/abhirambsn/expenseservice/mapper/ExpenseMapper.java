package com.abhirambsn.expenseservice.mapper;

import com.abhirambsn.expenseservice.dto.expense.CreateExpenseDto;
import com.abhirambsn.expenseservice.dto.expense.ExpenseResponseDto;
import com.abhirambsn.expenseservice.entity.Category;
import com.abhirambsn.expenseservice.entity.account.Account;
import com.abhirambsn.expenseservice.entity.expense.Expense;
import com.abhirambsn.expenseservice.entity.expense.ExpenseType;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {
    public ExpenseResponseDto toExpenseResponseDto(Expense expense) {
        return new ExpenseResponseDto(
                expense.getId(),
                expense.getName(),
                expense.getDescription(),
                expense.getCreatedDate(),
                expense.getAmount(),
                expense.getCreatedBy(),
                expense.getType(),
                expense.getFromAccount(),
                expense.getToAccount(),
                expense.getCategory(),
                expense.getCreatedAt(),
                expense.getUpdatedAt()
        );
    }

    public Expense mapToEntity(CreateExpenseDto dto) {
        Account fromAccount = new Account();
        fromAccount.setId(dto.getFromAccount());

        Account toAccount = new Account();

        if (dto.getType() == ExpenseType.EXPENSE) {
            toAccount = null;
        } else {
            toAccount.setId(dto.getToAccount());
        }

        Category category = new Category();
        category.setId(dto.getCategory());

        return Expense.builder()
                .name(dto.getName())
                .amount(dto.getAmount())
                .createdBy(dto.getCreatedBy())
                .description(dto.getDescription())
                .category(category)
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .type(dto.getType())
                .createdDate(dto.getCreatedDate())
                .id("")
                .build();
    }
}