package com.abhirambsn.expenseservice.entity.account;

import com.abhirambsn.expenseservice.entity.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "account_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AccountType extends BaseEntity {
    @Id
    private String id;

    private String name;

    private String description;

}
