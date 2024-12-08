package com.abhirambsn.expenseservice.service;

import com.abhirambsn.expenseservice.dto.account.AccountResponseDto;
import com.abhirambsn.expenseservice.dto.account.CreateAccountDto;
import com.abhirambsn.expenseservice.dto.expense.BulkCreateResponseDto;
import com.abhirambsn.expenseservice.entity.account.Account;
import com.abhirambsn.expenseservice.entity.account.AccountType;
import com.abhirambsn.expenseservice.mapper.AccountMapper;
import com.abhirambsn.expenseservice.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper mapper;

    public AccountService(AccountRepository accountRepository, AccountMapper mapper) {
        this.accountRepository = accountRepository;
        this.mapper = mapper;
    }

    public AccountResponseDto createAccount(CreateAccountDto dto) {
        Account account = mapper.mapToEntity(dto);
        account = accountRepository.save(account);
        return mapper.mapToResponseDto(account);
    }

    public BulkCreateResponseDto bulkCreateAccounts(List<CreateAccountDto> accounts) {
        try {
            List<String> ids = accounts.parallelStream()
                    .map(this::createAccount)
                    .map(AccountResponseDto::getId)
                    .toList();
            return BulkCreateResponseDto.builder()
                    .entity("accounts")
                    .count(ids.size())
                    .ids(ids)
                    .build();
        } catch (Exception e) {
            log.error("e: ", e);
            return null;
        }
    }

    public AccountResponseDto getAccount(String id) {
        Account account = accountRepository.findById(id).orElseThrow();
        return mapper.mapToResponseDto(account);
    }

    public List<AccountResponseDto> getAllAccounts(int top, int skip, String orderBy) {
        try {
            if (orderBy == null) {
                orderBy = "updatedAt";
            }
            log.info("Fetching all expenses with top: {}, skip: {}, orderBy: {}", top, skip, orderBy);
            if (top == 0) {
                top = 30;
            }
            Pageable pageable = PageRequest.of(skip / top, top);
            return accountRepository.findAllWithFilter(top, skip, orderBy, pageable).stream()
                    .map(mapper::mapToResponseDto)
                    .toList();
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public AccountResponseDto updateAccount(String id, CreateAccountDto dto) {
        Account account = accountRepository.findById(id).orElseThrow();
        if (dto.getName() != null) {
            account.setName(dto.getName());
        }
        if (dto.getOwnerId() != null) {
            account.setOwnerId(dto.getOwnerId());
        }
        if (dto.getTypeId() != null) {
            AccountType type = new AccountType();
            type.setId(dto.getTypeId());
            account.setType(type);
        }
        account = accountRepository.save(account);
        return mapper.mapToResponseDto(account);
    }

    public void deleteAccount(String id) {
        try {
            accountRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Account not found");
        }
    }
}
