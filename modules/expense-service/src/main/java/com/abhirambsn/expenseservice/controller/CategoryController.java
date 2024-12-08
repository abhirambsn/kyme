package com.abhirambsn.expenseservice.controller;

import com.abhirambsn.expenseservice.dto.CreateCategoryDto;
import com.abhirambsn.expenseservice.dto.expense.BulkCreateResponseDto;
import com.abhirambsn.expenseservice.service.CategoryService;
import com.abhirambsn.expenseservice.util.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> createCategory(
            @RequestBody CreateCategoryDto name
    ) {
        try {
            return JsonResponse.generateResponse(
                    "Category created",
                    HttpStatus.CREATED,
                    categoryService.createCategory(name)
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error creating category");
        }
    }

    @PostMapping("/$bulk")
    public ResponseEntity<Object> bulkCreateCategory(
            @RequestBody List<CreateCategoryDto> categories
    ) {
        BulkCreateResponseDto response = categoryService.bulkCreateCategories(categories);
        log.info("Bulk Created {} Categories", response.getCount());

        try {
            return JsonResponse.generateResponse(
                    "Bulk created " + response.getCount() + " categories",
                    HttpStatus.CREATED,
                    response
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error creating categories");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategory(
            @PathVariable String id
    ) {
        try {
            return JsonResponse.generateResponse(
                    "Category fetched",
                    HttpStatus.OK,
                    categoryService.getCategory(id)
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error fetching category");
        }
    }

    @GetMapping("/")
    public ResponseEntity<Object> getAllCategories() {
        try {
            return JsonResponse.generateResponse(
                    "Categories fetched",
                    HttpStatus.OK,
                    categoryService.getAllCategories()
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error fetching categories");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCategory(
            @PathVariable String id,
            @RequestBody CreateCategoryDto name
    ) {
        try {
            return JsonResponse.generateResponse(
                    "Category updated",
                    HttpStatus.OK,
                    categoryService.updateCategory(id, name)
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error updating category");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(
            @PathVariable String id
    ) {
        try {
            categoryService.deleteCategory(id);
            return JsonResponse.generateResponse(
                    "Category deleted",
                    HttpStatus.OK,
                    null
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.badRequest().body("Error deleting category");
        }
    }
}
