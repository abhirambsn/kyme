package com.abhirambsn.importexportservice.service;

import com.abhirambsn.importexportservice.dto.CreateJobRequestDto;
import com.abhirambsn.importexportservice.dto.JobResponseDto;
import com.abhirambsn.importexportservice.entity.Job;
import com.abhirambsn.importexportservice.entity.JobStatus;
import com.abhirambsn.importexportservice.mapper.JobMapper;
import com.abhirambsn.importexportservice.repository.JobRequestRepository;
import com.abhirambsn.importexportservice.tenant.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class JobService {
    private final JobRequestRepository jobRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JobMapper mapper;

    public JobService(
            JobRequestRepository jobRepository,
            KafkaTemplate<String, String> kafkaTemplate,
            JobMapper mapper
    ) {
        this.jobRepository = jobRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    public JobResponseDto createJob(CreateJobRequestDto jobRequest, String tenantId) {
        Job newJob = mapper.mapToEntity(jobRequest, tenantId);
        newJob.setStatus(JobStatus.PENDING);
        newJob = jobRepository.save(newJob);
        kafkaTemplate.send("jobs", newJob.getId());
        return mapper.mapToResponseDto(newJob);
    }

    public JobResponseDto getJob(String id) throws NoSuchElementException {
        return jobRepository.findById(id)
                .map(mapper::mapToResponseDto)
                .orElseThrow();
    }

    public List<JobResponseDto> getJobs(String tenantId) {
        List<Job> tenantJobs = jobRepository.findAllByTenantId(tenantId);
        return tenantJobs.stream().map(mapper::mapToResponseDto).toList();
    }

    public boolean cancelJob(String jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow();
        if (job.getStatus() != JobStatus.PENDING) {
            return false;
        }
        job.setStatus(JobStatus.CANCELLED);
        jobRepository.save(job);
        return true;
    }

}
