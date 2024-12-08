package com.abhirambsn.expenseservice.controller;

import com.abhirambsn.expenseservice.dto.account.CreateAccountDto;
import com.abhirambsn.expenseservice.dto.expense.BulkCreateResponseDto;
import com.abhirambsn.expenseservice.service.AccountService;
import com.abhirambsn.expenseservice.util.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> createAccount(
            @RequestBody CreateAccountDto dto
    ) {
        log.info("Creating Account");
        return JsonResponse.generateResponse(
                "Account Created",
                HttpStatus.CREATED,
                accountService.createAccount(dto)
        );
    }
    @PostMapping("/$bulk")
    public ResponseEntity<Object> bulkCreateAccount(
            @RequestBody List<CreateAccountDto> accounts
    ) {
        BulkCreateResponseDto response = accountService.bulkCreateAccounts(accounts);
        log.info("Bulk Created {} Accounts", response.getCount());

        try {
            return JsonResponse.generateResponse(
                    "Bulk created " + response.getCount() + " Accounts",
                    HttpStatus.CREATED,
                    response
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error creating categories");
        }
    }


    @GetMapping("/")
    public ResponseEntity<Object> getAllAccounts(
        @RequestParam(value = "$top", required = false, defaultValue = "30") int top,
        @RequestParam(value = "$skip", required = false, defaultValue = "0") int skip,
        @RequestParam(value = "$orderby", required = false) String orderBy
    ) {
        log.info("Fetched all Accounts");
        return JsonResponse.generateResponse(
                "",
                HttpStatus.OK,
                accountService.getAllAccounts(top, skip, orderBy)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAccount(
            @PathVariable String id
    ) {
        log.info("Fetched Account with ID: {}", id);
        return JsonResponse.generateResponse(
                "",
                HttpStatus.OK,
                accountService.getAccount(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAccount(
            @PathVariable String id,
            @RequestBody CreateAccountDto dto
    ) {
        log.info("Updating Account with ID: {}", id);
        return JsonResponse.generateResponse(
                "Account Updated",
                HttpStatus.OK,
                accountService.updateAccount(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAccount(
            @PathVariable String id
    ) {
        log.info("Deleting Account with ID: {}", id);
        accountService.deleteAccount(id);
        return JsonResponse.generateResponse(
                "Account Deleted",
                HttpStatus.OK,
                null
        );
    }


}
