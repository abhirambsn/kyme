package com.abhirambsn.expenseservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class CreateCategoryDto {
    private String name;
}
