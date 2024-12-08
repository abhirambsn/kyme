package com.abhirambsn.expenseservice.mapper;

import com.abhirambsn.expenseservice.dto.account.AccountTypeResponseDto;
import com.abhirambsn.expenseservice.dto.account.CreateAccountTypeDto;
import com.abhirambsn.expenseservice.entity.account.AccountType;
import org.springframework.stereotype.Component;

@Component
public class AccountTypeMapper {
    public AccountTypeResponseDto mapToResponseDto(AccountType accountType) {
        return AccountTypeResponseDto.builder()
                .id(accountType.getId())
                .name(accountType.getName())
                .description(accountType.getDescription())
                .build();
    }

    public AccountType mapToEntity(CreateAccountTypeDto dto) {
        return AccountType.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }
}
