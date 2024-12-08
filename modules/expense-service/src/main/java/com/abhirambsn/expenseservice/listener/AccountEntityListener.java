package com.abhirambsn.expenseservice.listener;

import com.abhirambsn.expenseservice.entity.account.Account;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AccountEntityListener extends AbstractMongoEventListener<Account> {
    @Override
    public void onBeforeConvert(@NonNull BeforeConvertEvent<Account> event) {
        if (event.getSource().getId().isEmpty()) {
            event.getSource().setId(UUID.randomUUID().toString());
            event.getSource().setCreatedAt(LocalDateTime.now());
            event.getSource().setUpdatedAt(LocalDateTime.now());
        }
    }
}
