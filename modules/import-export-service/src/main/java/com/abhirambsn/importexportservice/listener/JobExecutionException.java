package com.abhirambsn.importexportservice.listener;

import com.abhirambsn.importexportservice.entity.Job;
import lombok.Getter;

@Getter
public class JobExecutionException extends RuntimeException {
    private final Job job;

    public JobExecutionException(String message, Job job) {
        super(message);
        this.job = job;
    }
}
