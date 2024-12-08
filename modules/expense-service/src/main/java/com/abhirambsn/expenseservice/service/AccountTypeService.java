package com.abhirambsn.expenseservice.service;

import com.abhirambsn.expenseservice.dto.account.AccountTypeResponseDto;
import com.abhirambsn.expenseservice.dto.account.CreateAccountTypeDto;
import com.abhirambsn.expenseservice.dto.expense.BulkCreateResponseDto;
import com.abhirambsn.expenseservice.entity.account.AccountType;
import com.abhirambsn.expenseservice.mapper.AccountTypeMapper;
import com.abhirambsn.expenseservice.repository.AccountTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccountTypeService {
    private final AccountTypeRepository accountTypeRepository;
    private final AccountTypeMapper mapper;

    public AccountTypeService(AccountTypeRepository accountTypeRepository, AccountTypeMapper mapper) {
        this.accountTypeRepository = accountTypeRepository;
        this.mapper = mapper;
    }

    public AccountTypeResponseDto createAccountType(CreateAccountTypeDto dto) {
        try {
            return mapper.mapToResponseDto(accountTypeRepository.save(mapper.mapToEntity(dto)));
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public BulkCreateResponseDto bulkCreateAccountTypes(List<CreateAccountTypeDto> accountTypes) {
        try {
            List<String> ids = accountTypes.parallelStream()
                    .map(this::createAccountType)
                    .map(AccountTypeResponseDto::getId)
                    .toList();
            return BulkCreateResponseDto.builder()
                    .entity("accountTypes")
                    .count(ids.size())
                    .ids(ids)
                    .build();
        } catch (Exception e) {
            log.error("e: ", e);
            return null;
        }
    }

    public AccountTypeResponseDto getAccountType(String id) {
        try {
            return mapper.mapToResponseDto(accountTypeRepository.findById(id).orElseThrow());
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public List<AccountTypeResponseDto> getAllAccountTypes() {
        try {
            return accountTypeRepository.findAll().stream()
                    .map(mapper::mapToResponseDto)
                    .toList();
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public AccountTypeResponseDto updateAccountType(String id, CreateAccountTypeDto dto) {
        try {
            AccountType accountType = accountTypeRepository.findById(id).orElseThrow();
            accountType.setId(id);
            if (dto.getName() != null) {
                accountType.setName(dto.getName());
            }
            if (dto.getDescription() != null) {
                accountType.setDescription(dto.getDescription());
            }
            return mapper.mapToResponseDto(accountTypeRepository.save(accountType));
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public void deleteAccountType(String id) {
        try {
            accountTypeRepository.deleteById(id);
        } catch (Exception e) {
            log.error("e: ", e);
        }
    }
}
