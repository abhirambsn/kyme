package com.abhirambsn.importexportservice.dto;

import com.abhirambsn.importexportservice.entity.DataQuery;
import com.abhirambsn.importexportservice.entity.FileType;
import com.abhirambsn.importexportservice.entity.JobType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateJobRequestDto {
    @NonNull
    private String name;

    @NonNull
    private JobType jobType;

    @NonNull
    private FileType fileType;

    @NonNull
    private String entity;

    @NonNull
    private DataQuery query;

    private String fileUrl;

    @NonNull
    private String createdBy;
}
