package com.abhirambsn.expenseservice.dto.account;

import com.abhirambsn.expenseservice.entity.account.AccountType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountResponseDto {
    private String id;
    private String name;
    private String ownerId;
    private AccountType type;
}
