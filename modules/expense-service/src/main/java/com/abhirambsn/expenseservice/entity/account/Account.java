package com.abhirambsn.expenseservice.entity.account;

import com.abhirambsn.expenseservice.entity.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "accounts")
public class Account extends BaseEntity {
    @Id
    private String id;

    private String name;

    private String ownerId;

    @DBRef
    private AccountType type;
}
