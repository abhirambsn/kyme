package com.abhirambsn.expenseservice.controller;

import com.abhirambsn.expenseservice.dto.expense.BulkCreateResponseDto;
import com.abhirambsn.expenseservice.dto.expense.CreateExpenseDto;
import com.abhirambsn.expenseservice.dto.expense.ExpenseResponseDto;
import com.abhirambsn.expenseservice.entity.expense.Expense;
import com.abhirambsn.expenseservice.service.ExpenseService;
import com.abhirambsn.expenseservice.util.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> createExpense(@RequestBody CreateExpenseDto dto) {
        ExpenseResponseDto expense = expenseService.createExpense(dto);
        log.info("Expense Created with ID: {}", expense.getId());
        try {
            return JsonResponse.generateResponse(
                    "Expense Created",
                    HttpStatus.CREATED,
                    expense
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/$bulk")
    public ResponseEntity<Object> bulkCreateExpense(@RequestBody List<CreateExpenseDto> expenses) {
        BulkCreateResponseDto response = expenseService.bulkCreateExpenses(expenses);
        log.info("Bulk Created {} Expenses", response.getCount());

        return JsonResponse.generateResponse(
                "Bulk Created " + response.getCount() + " Expenses",
                HttpStatus.CREATED,
                response
        );
    }

    @GetMapping("/")
    public ResponseEntity<Object> getAllExpenses(
            @RequestParam(value = "$top", required = false, defaultValue = "30") int top,
            @RequestParam(value = "$skip", required = false, defaultValue = "0") int skip,
            @RequestParam(value = "$filter", required = false) String filter,
            @RequestParam(value = "$orderBy", required = false) String orderBy
    ) {
        log.info("Fetched all Expenses");

        return JsonResponse.generateResponse(
                "",
                HttpStatus.OK,
                expenseService.getExpenses(top, skip, orderBy, filter)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getExpense(
            @PathVariable String id
    ) {
        log.info("Fetched Expense with ID: {}", id);
        return JsonResponse.generateResponse(
                "",
                HttpStatus.OK,
                expenseService.getExpense(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateExpense(
            @RequestBody Expense expense
    ) {
        log.info("Updating Expense with ID: {}", expense.getId());
        return JsonResponse.generateResponse(
                "Expense Updated",
                HttpStatus.OK,
                expenseService.updateExpense(expense)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteExpense(
            @PathVariable String id
    ) {
        log.info("Deleting Expense with ID: {}", id);
        expenseService.deleteExpense(id);
        return JsonResponse.generateResponse(
                "Expense Deleted",
                HttpStatus.OK,
                null
        );
    }

    @GetMapping("/balance/{accountId}")
    public ResponseEntity<Object> getBalanceByAccount(
            @PathVariable String accountId
    ) {
        log.info("Fetched Balance for Account with ID: {}", accountId);
        return JsonResponse.generateResponse(
                "",
                HttpStatus.OK,
                expenseService.getAccountBalance(accountId)
        );
    }
}
