package com.abhirambsn.expenseservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Category extends BaseEntity {
    @Id
    private String id;

    private String name;
}
