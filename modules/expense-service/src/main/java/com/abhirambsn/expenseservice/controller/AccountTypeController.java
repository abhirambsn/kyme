package com.abhirambsn.expenseservice.controller;

import com.abhirambsn.expenseservice.dto.account.CreateAccountTypeDto;
import com.abhirambsn.expenseservice.dto.expense.BulkCreateResponseDto;
import com.abhirambsn.expenseservice.service.AccountTypeService;
import com.abhirambsn.expenseservice.util.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/accountTypes")
public class AccountTypeController {
    private final AccountTypeService accountTypeService;

    public AccountTypeController(AccountTypeService accountTypeService) {
        this.accountTypeService = accountTypeService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> createAccountType(
            @RequestBody CreateAccountTypeDto dto
    ) {
        try {
            return JsonResponse.generateResponse(
                    "Account type created successfully",
                    HttpStatus.CREATED,
                    accountTypeService.createAccountType(dto)
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error creating account type");
        }
    }

    @PostMapping("/$bulk")
    public ResponseEntity<Object> bulkCreateAccountType(
            @RequestBody List<CreateAccountTypeDto> accountTypes
    ) {
        BulkCreateResponseDto response = accountTypeService.bulkCreateAccountTypes(accountTypes);
        log.info("Bulk Created {} Account Types", response.getCount());

        try {
            return JsonResponse.generateResponse(
                    "Bulk created " + response.getCount() + " Account Types",
                    HttpStatus.CREATED,
                    response
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error creating categories");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAccountType(
            @PathVariable String id
    ) {
        try {
            return JsonResponse.generateResponse(
                    "Account type fetched",
                    HttpStatus.OK,
                    accountTypeService.getAccountType(id)
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error fetching account type");
        }
    }

    @GetMapping("/")
    public ResponseEntity<Object> getAllAccountTypes() {
        try {
            return JsonResponse.generateResponse(
                    "Account types fetched",
                    HttpStatus.OK,
                    accountTypeService.getAllAccountTypes()
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error fetching account types");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAccountType(
            @PathVariable String id,
            @RequestBody CreateAccountTypeDto dto
    ) {
        try {
            return JsonResponse.generateResponse(
                    "Account type updated",
                    HttpStatus.OK,
                    accountTypeService.updateAccountType(id, dto)
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error updating account type");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAccountType(
            @PathVariable String id
    ) {
        try {
            accountTypeService.deleteAccountType(id);
            return JsonResponse.generateResponse(
                    "Account type deleted",
                    HttpStatus.OK,
                    null
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error deleting account type");
        }
    }
}
