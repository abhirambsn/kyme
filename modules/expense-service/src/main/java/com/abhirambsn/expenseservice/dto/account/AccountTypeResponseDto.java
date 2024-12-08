package com.abhirambsn.expenseservice.dto.account;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountTypeResponseDto {
    private String id;
    private String name;
    private String description;
}
