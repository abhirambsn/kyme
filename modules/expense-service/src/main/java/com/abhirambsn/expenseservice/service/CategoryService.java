package com.abhirambsn.expenseservice.service;

import com.abhirambsn.expenseservice.dto.CreateCategoryDto;
import com.abhirambsn.expenseservice.dto.expense.BulkCreateResponseDto;
import com.abhirambsn.expenseservice.entity.Category;
import com.abhirambsn.expenseservice.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CreateCategoryDto categoryDto) {
        try {
            Category category = Category.builder()
                    .name(categoryDto.getName())
                    .build();
            return categoryRepository.save(category);
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public BulkCreateResponseDto bulkCreateCategories(List<CreateCategoryDto> categories) {
        try {
            List<String> ids = categories.parallelStream()
                    .map(this::createCategory)
                    .map(Category::getId)
                    .toList();
            return BulkCreateResponseDto.builder()
                    .entity("categories")
                    .count(ids.size())
                    .ids(ids)
                    .build();
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public Category getCategory(String id) {
        try {
            return categoryRepository.findById(id).orElseThrow();
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(String id, CreateCategoryDto categoryDto) {
        try {
            Category category = categoryRepository.findById(id).orElseThrow();
            category.setName(categoryDto.getName());
            return categoryRepository.save(category);
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public void deleteCategory(String id) {
        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            log.error("e: ", e);
        }
    }
}
