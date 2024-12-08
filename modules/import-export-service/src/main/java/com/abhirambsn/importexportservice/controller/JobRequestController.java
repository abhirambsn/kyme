package com.abhirambsn.importexportservice.controller;

import com.abhirambsn.importexportservice.dto.CreateJobRequestDto;
import com.abhirambsn.importexportservice.dto.JobResponseDto;
import com.abhirambsn.importexportservice.service.JobService;
import com.abhirambsn.importexportservice.tenant.TenantContext;
import com.abhirambsn.importexportservice.util.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@RestController
public class JobRequestController {
    private final JobService jobService;
    private final TenantContext tenantContext;

    public JobRequestController(JobService jobService, TenantContext tenantContext) {
        this.jobService = jobService;
        this.tenantContext = tenantContext;
    }

    @PostMapping("/")
    public ResponseEntity<Object> createJob(
            @RequestBody CreateJobRequestDto dto
    ) {
       try {
           String tenantId = tenantContext.getTenantId();
           if (tenantId == null || tenantId.isEmpty()) {
               return ResponseEntity.badRequest().body("Tenant ID not found or not present in the Header X-TenantID");
           }
           JobResponseDto response = jobService.createJob(dto, tenantId);
           return JsonResponse.generateResponse(
                     "Job Created",
                     HttpStatus.CREATED,
                     response
           );
       } catch (Exception e) {
           return ResponseEntity.internalServerError().body("Error creating job");
       }
    }

    @GetMapping("/")
    public ResponseEntity<Object> getAllJobs() {
        String tenantId = tenantContext.getTenantId();
        if (tenantId == null || tenantId.isEmpty()) {
            return ResponseEntity.badRequest().body("Tenant ID not found or not present in the Header X-TenantID");
        }
        return JsonResponse.generateResponse(
                "Fetched all Jobs",
                HttpStatus.OK,
                jobService.getJobs(tenantId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getJob(
            @PathVariable String id
    ) {
        try {
            JobResponseDto job = jobService.getJob(id);
            return JsonResponse.generateResponse(
                    "Fetched Job",
                    HttpStatus.OK,
                    job
            );
        } catch (NoSuchElementException e) {
            log.error("job not found: ", e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> cancelJob(
            @PathVariable String id
    ) {
        try {
            boolean isCancelled = jobService.cancelJob(id);
            if (!isCancelled) {
                return JsonResponse.generateResponse(
                        "Only pending jobs can be cancelled",
                        HttpStatus.BAD_REQUEST,
                        null
                );
            }
            return JsonResponse.generateResponse(
                    "Job Cancelled",
                    HttpStatus.OK,
                    ""
            );
        } catch (NoSuchElementException e) {
            log.error("job not found: ", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/template")
    public ResponseEntity<?> downloadTemplate(
            @RequestParam("type") String type,
            @RequestParam("entity") String entity
    ) {
        return (ResponseEntity<?>) ResponseEntity.ok();
    }
}
