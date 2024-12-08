package com.abhirambsn.importexportservice.entity;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequest {
    private JobType jobType;
    private FileType fileType;
    private String entity;
    private DataQuery query;
    private String fileUrl;
}
