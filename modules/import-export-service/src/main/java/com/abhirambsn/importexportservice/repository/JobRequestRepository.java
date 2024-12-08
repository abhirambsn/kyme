package com.abhirambsn.importexportservice.repository;

import com.abhirambsn.importexportservice.entity.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobRequestRepository extends MongoRepository<Job, String> {
    List<Job> findAllByTenantId(String tenantId);
}
