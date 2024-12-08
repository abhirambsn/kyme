package com.abhirambsn.importexportservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "jobs")
public class Job extends BaseEntity {
    @Id
    private String id;

    private String name;

    private JobRequest request;
    private JobStatus status;
    private String tenantId;
    private String createdBy;

    private String downloadUrl;
}
