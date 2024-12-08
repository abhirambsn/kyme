package com.abhirambsn.expenseservice.listener;

import com.abhirambsn.expenseservice.entity.expense.Expense;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ExpenseEntityListener extends AbstractMongoEventListener<Expense> {
    @Override
    public void onBeforeConvert(@NonNull BeforeConvertEvent<Expense> event) {
        String id = event.getSource().getId();
        if (id == null || id.isEmpty()) {
            event.getSource().setId(UUID.randomUUID().toString());
            event.getSource().setCreatedAt(LocalDateTime.now());
            event.getSource().setUpdatedAt(LocalDateTime.now());
        }
    }
}
