package com.abhirambsn.expenseservice.dto.expense;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkCreateResponseDto {
    private String entity;
    private long count;
    private List<String> ids;
}
