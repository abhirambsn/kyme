package com.abhirambsn.importexportservice.mapper;

import com.abhirambsn.importexportservice.dto.CreateJobRequestDto;
import com.abhirambsn.importexportservice.dto.JobResponseDto;
import com.abhirambsn.importexportservice.entity.Job;
import com.abhirambsn.importexportservice.entity.JobRequest;
import org.springframework.stereotype.Component;


@Component
public class JobMapper {
    public JobResponseDto mapToResponseDto(Job job) {
        return JobResponseDto.builder()
                .id(job.getId())
                .name(job.getName())
                .request(job.getRequest())
                .status(job.getStatus())
                .tenantId(job.getTenantId())
                .createdBy(job.getCreatedBy())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .downloadUrl(job.getDownloadUrl())
                .build();
    }

    public Job mapToEntity(CreateJobRequestDto jobRequestDto, String tenantId) {
        JobRequest jobRequest = JobRequest.builder()
                .jobType(jobRequestDto.getJobType())
                .fileType(jobRequestDto.getFileType())
                .entity(jobRequestDto.getEntity())
                .query(jobRequestDto.getQuery())
                .fileUrl(jobRequestDto.getFileUrl()).
                build();
        return Job.builder()
                .request(jobRequest)
                .name(jobRequestDto.getName())
                .createdBy(jobRequestDto.getCreatedBy())
                .tenantId(tenantId)
                .build();
    }
}
