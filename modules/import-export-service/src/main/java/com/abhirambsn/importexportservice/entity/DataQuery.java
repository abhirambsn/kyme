package com.abhirambsn.importexportservice.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DataQuery {
    private int top;
    private int skip;
    private String filter;
    private String orderBy;
}
