package com.abhirambsn.expenseservice.mapper;

import com.abhirambsn.expenseservice.dto.account.AccountResponseDto;
import com.abhirambsn.expenseservice.dto.account.CreateAccountDto;
import com.abhirambsn.expenseservice.entity.account.Account;
import com.abhirambsn.expenseservice.entity.account.AccountType;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountResponseDto mapToResponseDto(Account account) {
        return new AccountResponseDto(
                account.getId(),
                account.getName(),
                account.getOwnerId(),
                account.getType()
        );
    }

    public Account mapToEntity(CreateAccountDto dto) {
        AccountType acType = new AccountType();
        acType.setId(dto.getTypeId());
        return new Account(
                "",
                dto.getName(),
                dto.getOwnerId(),
                acType
        );
    }
}
