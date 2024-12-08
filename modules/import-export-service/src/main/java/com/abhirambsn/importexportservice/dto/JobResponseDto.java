package com.abhirambsn.importexportservice.dto;

import com.abhirambsn.importexportservice.entity.JobRequest;
import com.abhirambsn.importexportservice.entity.JobStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class JobResponseDto {
    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private JobRequest request;

    @NonNull
    private JobStatus status;

    @NonNull
    private String tenantId;

    @NonNull
    private String createdBy;

    private String downloadUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
