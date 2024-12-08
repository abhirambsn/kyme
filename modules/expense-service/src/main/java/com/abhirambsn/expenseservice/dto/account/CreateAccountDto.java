package com.abhirambsn.expenseservice.dto.account;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountDto {
    private String name;
    private String ownerId;
    private String typeId;
}
