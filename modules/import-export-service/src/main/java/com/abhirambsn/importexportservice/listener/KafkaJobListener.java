package com.abhirambsn.importexportservice.listener;

import com.abhirambsn.importexportservice.entity.Job;
import com.abhirambsn.importexportservice.entity.JobStatus;
import com.abhirambsn.importexportservice.repository.JobRequestRepository;
import com.abhirambsn.importexportservice.service.JobProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@Component
public class KafkaJobListener {
    private final JobRequestRepository jobRequestRepository;
    private final JobProcessor jobProcessor;

    public KafkaJobListener(JobRequestRepository jobRequestRepository, JobProcessor jobProcessor) {
        this.jobRequestRepository = jobRequestRepository;
        this.jobProcessor = jobProcessor;
    }

    @KafkaListener(topics = "jobs", groupId = "jobs_group")
    public void processJob(String jobId) {
        try {
            Job job = jobRequestRepository.findById(jobId).orElseThrow();
            if (!checkJob(job)) {
                throw new IllegalStateException("Only Jobs in PENDING state will be processed");
            }
            updateJobStatus(job, JobStatus.PROCESSING);
            Job result = jobProcessor.process(job);
            jobRequestRepository.save(result);
        } catch (NoSuchElementException e) {
            log.error("Job not found: ", e);
        } catch (IllegalStateException e) {
            log.error("Illegal state: ", e);
        } catch (JobExecutionException e) {
            log.error("Error in job processing: ", e);
            updateJobStatus(e.getJob(), JobStatus.FAILED);
        } catch (Exception e) {
            log.error("Unknown Exception: ", e);
        }
    }

    private void updateJobStatus(Job job, JobStatus status) {
        job.setStatus(status);
        jobRequestRepository.save(job);
    }

    private boolean checkJob(Job job) {
        return job.getStatus().equals(JobStatus.PENDING);
    }
}
