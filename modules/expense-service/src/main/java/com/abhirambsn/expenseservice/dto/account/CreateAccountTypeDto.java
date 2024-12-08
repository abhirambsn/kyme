package com.abhirambsn.expenseservice.dto.account;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountTypeDto {
    private String name;
    private String description;
}
