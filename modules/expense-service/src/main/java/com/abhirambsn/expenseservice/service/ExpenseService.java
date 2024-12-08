package com.abhirambsn.expenseservice.service;

import com.abhirambsn.expenseservice.dto.expense.BulkCreateResponseDto;
import com.abhirambsn.expenseservice.dto.expense.CreateExpenseDto;
import com.abhirambsn.expenseservice.dto.expense.ExpenseResponseDto;
import com.abhirambsn.expenseservice.entity.NetAmount;
import com.abhirambsn.expenseservice.entity.SearchQuery;
import com.abhirambsn.expenseservice.entity.account.Account;
import com.abhirambsn.expenseservice.entity.Category;
import com.abhirambsn.expenseservice.entity.expense.Expense;
import com.abhirambsn.expenseservice.entity.expense.ExpenseType;
import com.abhirambsn.expenseservice.mapper.ExpenseMapper;
import com.abhirambsn.expenseservice.repository.ExpenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper mapper;
    private final SearchService searchService;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseMapper mapper, SearchService searchService) {
        this.expenseRepository = expenseRepository;
        this.mapper = mapper;
        this.searchService = searchService;
    }

    public ExpenseResponseDto createExpense(CreateExpenseDto dto) {
        try {
            Expense expense = mapper.mapToEntity(dto);
            expense = expenseRepository.save(expense);
            return mapper.toExpenseResponseDto(expense);
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public BulkCreateResponseDto bulkCreateExpenses(List<CreateExpenseDto> expenses) {
        try {
            List<Expense> allExpenses = expenses.parallelStream()
                    .map(mapper::mapToEntity)
                    .toList();

            List<String> ids = expenseRepository.saveAll(allExpenses).stream().map(Expense::getId).toList();

            return BulkCreateResponseDto.builder()
                    .count(ids.size())
                    .entity("expenses")
                    .ids(ids).build();
        } catch (Exception e) {
            log.error("e: ", e);
            return null;
        }
    }

    public List<ExpenseResponseDto> getExpenses(int top, int skip, String orderBy, String filter) {
        SearchQuery query = SearchQuery.builder()
                .top(top)
                .skip(skip)
                .filter(filter)
                .orderBy(orderBy)
                .build();

        List<ExpenseResponseDto> results = (List<ExpenseResponseDto>) searchService.search("expenses", query);
        return results;
    }

    public ExpenseResponseDto getExpense(String id) {
        try {
            Expense expense = expenseRepository.findById(id).orElse(null);
            if (expense == null) {
                return null;
            }
            return mapper.toExpenseResponseDto(expense);
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public ExpenseResponseDto updateExpense(Expense expense) {
        try {
            Expense updatedExpense = expenseRepository.save(expense);
            return mapper.toExpenseResponseDto(updatedExpense);
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public void deleteExpense(String id) {
        try {
            expenseRepository.deleteById(id);
        } catch (Exception e) {
            log.error("e: ", e);
        }
    }

    public NetAmount getAccountBalance(String accountId) {
        try {
            return expenseRepository.getTotalExpenseByAccount(accountId);
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public List<Expense> getExpensesByCategory(String categoryId) {
        try {
            return expenseRepository.findAllByCategoryId(categoryId);
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public List<Expense> getExpensesByAccount(String accountId) {
        try {
            return expenseRepository.findExpenseByFromAccountIdOrToAccountId(accountId, accountId);
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }
}