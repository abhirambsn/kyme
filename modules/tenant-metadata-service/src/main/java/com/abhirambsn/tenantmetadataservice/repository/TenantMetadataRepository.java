package com.abhirambsn.tenantmetadataservice.repository;

import com.abhirambsn.tenantmetadataservice.entity.TenantMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantMetadataRepository extends JpaRepository<TenantMetadata, String> {
    Optional<TenantMetadata> findBySlug(String slug);
}
