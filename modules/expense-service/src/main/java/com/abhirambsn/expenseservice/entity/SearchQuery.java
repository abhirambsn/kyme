package com.abhirambsn.expenseservice.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchQuery {
    private Integer top;
    private Integer skip;
    private String orderBy;
    private String filter;
}
